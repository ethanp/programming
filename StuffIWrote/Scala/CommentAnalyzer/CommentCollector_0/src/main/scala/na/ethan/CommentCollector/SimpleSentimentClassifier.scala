package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/22/14
 *
 * based on github.com/shekhargulati/day20-stanford-sentiment-analysis-demo
 *
 * Finds sentiment of the longest sentence passed into the function
 */

import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import java.util.Properties
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.ling.CoreAnnotations
import scala.collection.JavaConverters._

object SimpleSentimentClassifier extends App {
  val ehh = "I love this sentence so much, it makes me really happy."
  val ehhhh = "Fuck this shit, I hate everything that this sentence represents."
  val hmm = "I don't know, but I think this sentence is OK."
  def evaluateSentiment(str: String) {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
    val pipeline = new StanfordCoreNLP(props)
    var mainSentiment = 0
    if (str != null && str.length > 0) {
      var longest = 0
      val annotation = pipeline.process(str)
      for (sentence: CoreMap <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).asScala ) {
        val tree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
        val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
        val partText = sentence.toString
        // TODO: it should average them out or weight by sentence length or something...
        // at this point it does a lot of useless classification that won't be used
        if (partText.length > longest) {
          mainSentiment = sentiment
          longest = partText.length
        }
      }
    }
    println(mainSentiment)
  }
  evaluateSentiment(ehh)    // => 3: Happy
  evaluateSentiment(ehhhh)  // => 1: Angry
  evaluateSentiment(hmm)    // => 2: Medium
}
