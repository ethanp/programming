import java.io.{PrintWriter, File}
import java.util
import org.apache.mahout.cf.taste.common.TasteException
import org.apache.mahout.cf.taste.eval.{IRStatistics, RecommenderIRStatsEvaluator, RecommenderBuilder, RecommenderEvaluator}
import org.apache.mahout.cf.taste.impl.eval.{GenericRecommenderIRStatsEvaluator, AverageAbsoluteDifferenceRecommenderEvaluator}
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.{GenericItemBasedRecommender, GenericUserBasedRecommender}
import org.apache.mahout.cf.taste.impl.similarity.{EuclideanDistanceSimilarity, PearsonCorrelationSimilarity}
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.{Recommender, RecommendedItem}
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.common.RandomUtils
import scala.io.Source


/**
 * Base Source:
 * slideshare.net/Cataldo/apache-mahout-tutorial-recommendation-20132014
 */

object A extends App {

  // ensures consistency between different evaluation runs
  RandomUtils.useTestSeed()

  val model: DataModel = new FileDataModel(new File(
    "/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/fonts/src/main/scala/MoviesList.csv"

    // TODO: Doesn't work yet because it's expecting (usr,item,val) but I'm giving (usr,pt1,pt2,val)
    // looks like that's literally all it's going to need though
//    "/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/fonts/OutFileData.csv"
  ))

  val evaluator: RecommenderEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator
  val irEvaluator: RecommenderIRStatsEvaluator = new GenericRecommenderIRStatsEvaluator


    // TODO not sure why but this isn't working
//  val rmse: RecommenderEvaluator = new RMSEEvaluator()

  val recommenderBuilder: RecommenderBuilder = new RecommenderBuilder() {
    @Override
    def buildRecommender(model: DataModel): Recommender = {
      val similarity: UserSimilarity = new PearsonCorrelationSimilarity(model)

      // TODO try this one out too
      val euclideanSimilarity: UserSimilarity = new EuclideanDistanceSimilarity(model)

      // the first parameter is the number of neighbors
      val neighborhood: UserNeighborhood = new NearestNUserNeighborhood(2, similarity, model)

      // TODO y u no resolve constructor?
//      val a = new GenericItemBasedRecommender(model, euclideanSimilarity)

      new GenericUserBasedRecommender(model, neighborhood, similarity)
    }
  }

  // 70% training, evaluation on the whole dataset
  val score: Double = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0)
  println(s"Score: $score")

//  val rmseScore: Double = rmse.evaluate(recommenderBuilder, null, model, 0.7, 1.0)

  val irStats: IRStatistics = irEvaluator.evaluate(
    recommenderBuilder, null, model, null, 5,
    GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0
  )

  // precision @ 5, Recall @ 5, F1 (combination)
  println(
    s"""Precision: ${irStats.getPrecision},
       |Recall: ${irStats.getRecall},
       |F1: ${irStats.getF1Measure}""".stripMargin
  )

  import scala.collection.JavaConversions._
//  val recommendations: util.List[RecommendedItem] = recommender.recommend(1, 2)
//  recommendations foreach println
}

/**
 * process the raw files into something suitable for mahout
 * basically need to map to generate the data
 * and reduce if I want to plot it with R
 */
object CleanData extends App {
  val dirName = "/Users/Ethan/Downloads/afm 2"
  val files = new java.io.File(dirName).listFiles.filter(_.getName.endsWith(".afm"))
  val writer = new PrintWriter("OutFileData.csv")
  files.map(Source.fromFile(_).getLines())
  val files_data = files.zipWithIndex map { case (file, i) =>
    val lines = Source.fromFile(file).getLines()
    val correctLines = lines.filter(_ startsWith "KPX")
    correctLines.map(i + _.replaceFirst("KPX", "").replaceAll(" ", ",") + "\n").toList
  }
  val data = files_data.flatten
  val keys = data.map(s => {
    val p = s.split(",")
    s"${p(1)},${p(2)}"
  })
  val map = keys.groupBy {
    case i => i
  }
  println(map.toString())

//    formattedLines foreach writer.write
//  writer.close()
}
