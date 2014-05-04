package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import play.api.libs.ws.WS
import scala.concurrent.Future
import play.api.libs.json.{Json, JsValue}
import play.api.Play
import play.api.libs.ws.Response
import play.api.libs.json.JsString

object StockSentiment extends Controller {

  // EP: this is superfluous, I guess it's just an example of what one can do
  case class Tweet(text: String)

  /** EP: according to the ScalaDoc, this is strictly equivalent to:
    *
    *   implicit val tweetReads = (
    *      (_._ \ 'text).read[String]   // NOTE: _._ shouldn't have the period in the middle
    *   )(Tweet)                        // but that won't compile even though we're in a doc...
    *
    * What it actually does is define a Json -> Tweet parser so that later on we can do
    *
    *   (json \ "statuses").as[Seq[Tweet]]
    *
    */
  implicit val tweetReads = Json.reads[Tweet]

  def getTextSentiment(text: String): Future[Response] = {

    val sentimentApiUrl: String = Play.current.configuration.getString("sentiment.url").get

    // EP: WS stands for "Web Services" *not* "Web Sockets"
    // EP: They pass a Seq(text) even though there's only one thing because that's what the API expects.
    // --- www.playframework.com/documentation/2.2.x/ScalaWS
    WS.url(sentimentApiUrl) post Map("text" -> Seq(text))
  }

  def getAverageSentiment(responses: Seq[Response], label: String): Double = responses.map { response =>
    (response.json \\ label).head.as[Double]
  }.sum / responses.length.max(1) // avoid division by zero

  def loadSentimentFromTweets(json: JsValue): Seq[Future[Response]] =
    (json \ "statuses").as[Seq[Tweet]] map (tweet => getTextSentiment(tweet.text))

  def getTweets(symbol:String): Future[Response] = {

    // EP: find the url listed in the configuration file: API_LOC.com/api?q=%%24%s,
    // --- now we can use 'format()' to fill-in the %s in the URL with the symbol the user requested
    WS.url(Play.current.configuration.getString("tweet.url").get.format(symbol))
      .get().withFilter(response => response.status == OK) // EP: not sure what'll happen if it's /not/ == OK
  }


  def sentimentJson(sentiments: Seq[Response]) = {
    val neg = getAverageSentiment(sentiments, "neg")
    val neutral = getAverageSentiment(sentiments, "neutral")
    val pos = getAverageSentiment(sentiments, "pos")

    val response = Json.obj(
      "probability" -> Json.obj(
        "neg" -> neg,
        "neutral" -> neutral,
        "pos" -> pos
      )
    )

    val classification =
      if (neutral > 0.5)
        "neutral"
      else if (neg > pos)
        "neg"
      else
        "pos"

    response + ("label" -> JsString(classification))
  }

  /** EP:
    * this is totally the thing I've been trying and failing to implement

    * We want to send a response to an external-API-involving client Request obtained through
      a Web Socket, but we don't want to block at all while we wait to process the result, and
      we don't want the user to block at all either. So both interact completely non-blockingly
      by combining the Web Socket with Futures.

    * `@param symbol` must is the stock symbol they pass into the request box


  ===============
  | THE JOURNEY |
  ===============

    1. We got here in the execution starting in the index.coffeescript:

      """
      handleFlip = (container) ->
        ...
        # fetch stock details and tweet
        $.ajax
          url: "/sentiment/" + container.children(".flipper").attr("data-content")
          dataType: "json"
          context: container
          success: (data) ->
            ...
          error: (jqXHR, textStatus, error) ->
            ...
      """

    2. Then we go to the Routes file

      """
        GET     /sentiment/:symbol          controllers.StockSentiment.get(symbol)
      """

    3. This brings us to the StockSentiment Controller [this file] (note not the Application Controller)

      def get(symbol: String): Action[AnyContent] = Action.async { ... }

        which returns a JSON object containing

        {
          probability : {
            neg     : .23,
            neutral : ...,
            pos     : ...,
          },
          label : "neutral"   // or whatever
        }

    4. The index.coffeescript will then display the appropriate msg depending on the data.label

      e.g:

        detailsHolder.append($("<h4>").text("The tweets say HOLD!"))
        detailsHolder.append($("<img>").attr("src", "/assets/images/hold.png"))


    */

  def get(symbol: String) = Action.async {

    // EP: We're saying that once we get response from the Sentiment Analysis API,
    // --- we'll send the appropriate response (JSON data or error msg) to the client
    val futureStockSentiments: Future[SimpleResult] = for {

      // EP: `getTweets` gave us a Response from some fake Twitter fire-hose API containing
      // --- a JSON response in the Body, which we can retrieve with `tweets.json` later
      tweets <- getTweets(symbol) // get tweets that contain the stock symbol

      /**EP: here we
          1. retrieve the text from the Tweets
          2. Post the text to the Sentiment Analysis API
          3. store the Seq[Future[Response]] in a variable
       */
      futureSentiments = loadSentimentFromTweets(tweets.json) // queue web requests each tweets' sentiments

      // EP: this turns Seq[Future[Response]] into Future[Sequence[Response]]
      sentiments <- Future.sequence(futureSentiments)

    // EP: "for -> yield" is similar to "map" in that it means the result
    // --- inside the block gets automatically wrapped back into a Future
    } yield
      // EP: wrap the results in a JSON obj in the format expected by the CoffeeScripted client
      Ok(sentimentJson(sentiments))

    // EP: `recover` adds an exception handler for the event that the Future fails.
    // --- This is way cleaner than the way I've been doing it.
    futureStockSentiments.recover {
      case nsee: NoSuchElementException =>
        InternalServerError(Json.obj("error" -> JsString("Could not fetch the tweets")))
    }
  }
}
