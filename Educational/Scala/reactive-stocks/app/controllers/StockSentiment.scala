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

  implicit val tweetReads = Json.reads[Tweet]

  def getTextSentiment(text: String): Future[Response] =
    WS.url(Play.current.configuration.getString("sentiment.url").get) post Map("text" -> Seq(text))

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

  // EP: TODO this is totally the sort of thing I've been trying and failing to implement
  // --- TODO wait no, this *IS* **EXACTLY** what I've been trying to implement. Dey Jacked my Steez!
  // EP: we want to send a response to an external-API-involving client-request obtained through
  // --- a Web Socket, but we don't want to block at all while we wait to process the result, and
  // --- we don't want the user to block at all either. So both interact completely non-blockingly
  // --- by combining the Web Socket with Futures.
  // EP: `@param symbol` must be the stock symbol they pass into the request box
  def get(symbol: String): Action[AnyContent] = Action.async {

    // EP: We're saying that once we get
    val futureStockSentiments: Future[SimpleResult] = for {

      // EP: `getTweets` gave us a Response from some fake Twitter fire-hose API, which gives
      // --- is a JSON response in the body, which we can retrieve with `tweets.json`
      tweets <- getTweets(symbol) // get tweets that contain the stock symbol


      futureSentiments = loadSentimentFromTweets(tweets.json) // queue web requests each tweets' sentiments

      // EP: this seems to say, "Block until the contents of every Future contained within have arrived."
      sentiments <- Future.sequence(futureSentiments) // [EP: block, then] when the sentiment responses arrive, set them
    } yield Ok(sentimentJson(sentiments)) // EP: I think the `yield` means it gets automatically wrapped back into a Future

    // EP: `recover` adds an exception handler for the event that the Future fails.
    // --- This is way cleaner than the way I've been doing it.
    futureStockSentiments.recover {
      case nsee: NoSuchElementException =>
        InternalServerError(Json.obj("error" -> JsString("Could not fetch the tweets")))
    }
  }
}
