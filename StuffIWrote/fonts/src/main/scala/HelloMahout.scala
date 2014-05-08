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
import org.apache.mahout.cf.taste.similarity.{ItemSimilarity, UserSimilarity}
import org.apache.mahout.common.RandomUtils
import scala.collection.mutable
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
      val itemSimilarity: ItemSimilarity = new PearsonCorrelationSimilarity(model)

      // TODO try this one out too
      val euclideanSimilarity: UserSimilarity = new EuclideanDistanceSimilarity(model)

      // the first parameter is the number of neighbors
      val neighborhood: UserNeighborhood = new NearestNUserNeighborhood(2, similarity, model)

      val itemRecommender: Recommender = new GenericItemBasedRecommender(model, itemSimilarity)

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

//  import scala.collection.JavaConversions._
//  val recommendations: util.List[RecommendedItem] = recommender.recommend(1, 2)
//  recommendations foreach println
}

/**
 * Process the raw files into something suitable for mahout
 *
 * Also output a CSV of [Letter1, Letter2, Count, PairID] data, sorted by Count descending
 */
object CleanData extends App {
  val dirName = "/Users/Ethan/Downloads/afm 2"
  val files = new java.io.File(dirName).listFiles.filter(_.getName.endsWith(".afm"))

  mutable.TreeSet
  // ["font_id,letter,letter,value",...]
  val inCSV: Array[String] =
    files.zipWithIndex map { case (file, i) =>
      Source.fromFile(file).getLines()
        .filter(_ startsWith "KPX")
        .map(i + _.replaceFirst("KPX", "").replaceAll(" ", ",") + "\n")
        .toList
    } flatten

  /** outCSV : a csv file-string ready for ingestion by Mahout's FileDataModel constructor
    PairInfo : gives a unique id and a seen-count to each letter pair

    This algorithm is optimized for a single processor, but could use two mapreduce
    steps to accomplish the same thing with parallel mappers and reducers.
   */
  case class PairInfo(id: Int, var count: Int)
  val letterMap = mutable.Map.empty[String, PairInfo]
  val outCSV: String = inCSV.map { line =>
    val lineArray = line split ","
    val pair = lineArray.slice(1, 3).mkString(",")
    if (letterMap contains pair) letterMap(pair).count += 1
    else letterMap(pair) = PairInfo(letterMap.size, 1)
    List(lineArray(0), letterMap(pair).id, lineArray(3)).mkString(",")
  } mkString

  val modelDataWriter = new PrintWriter("OutFileData.csv")
  modelDataWriter write outCSV
  modelDataWriter.close()


  /** histogram : sorted csv histogram of letter pairs

     "highest,pair,count,id
      nextHighest,pair,count,id
      ... "
    */
  val histogram: String = letterMap.map { case (pair, info) =>
    (pair split ",") ++ Array(info.count.toString, info.id.toString)
  }.toList                                  // get rid of the pair_id
    .sortWith( _(2).toInt > _(2).toInt )    // sort by count, descending
    .map(_ mkString ",") mkString "\n"      // turn to CSV string

  println(histogram)                        // print to console

  val header = "FirstLetter,SecondLetter,Count,ID\n"
  val histogramWriter = new PrintWriter("KerningHistogram.csv")
  histogramWriter write header+histogram           // write to file
  histogramWriter.close()
}
