package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/22/14
 *
 * modified from example at github.com/shekhargulati/day20-stanford-sentiment-analysis-demo
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
  def evaluateSentiment(text: String) = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
    val pipeline = new StanfordCoreNLP(props)
    var avgSentiment = 0.0
    val length = text.length.asInstanceOf[Double]
    if (text != null && length > 0) {
      val annotation = pipeline.process(text)
      for (sentence: CoreMap <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).asScala) {
        val tree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
        val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
        val partText = sentence.toString
        avgSentiment += sentiment * partText.length.asInstanceOf[Double] / length
        println(avgSentiment)
      }
    } else println("can't analyze this crap")
    avgSentiment
  }
  evaluateSentiment(ehh)    // => 3: Happy
  evaluateSentiment(ehhhh)  // => 1: Angry
  evaluateSentiment(hmm)    // => 2: Medium
}
