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
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage
import org.apache.mahout.cf.taste.common.Weighting
import scala.collection.mutable
import scala.io.Source


/**
 * Based on: every Mahout tutorial out there
 */

object HelloMahout extends App {

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

      val userRecommender: Recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)

      // src1: http://books.dzone.com/articles/slope-one-recommender
      // src2: https://gist.github.com/tuxdna/5903814
      // not sure this'll just work out-of-the-box like this
      // TODO try using Weighting.WEIGHTED too
      val diffStorage: DiffStorage = new MemoryDiffStorage(model, Weighting.UNWEIGHTED, Long.MAX_VALUE)
      val slope1Recommender: Recommender = new SlopeOneRecommender(model, Weighting.UNWEIGHTED, Weighting.UNWEIGHTED, diffStorage)

      new CaachingRecommender(recommender)
    }
  }

  // 70% training, evaluation on the whole dataset
  val score: Double = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0)
  println(s"Score: $score")

//  val rmseScore: Double = rmse.evaluate(recommenderBuilder, null, model, 0.7, 1.0)

  // Note: this is more for a "boolean" data model situation, where there are no notions of preference value
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

  // ["font_id,letter,letter,value",...]
  val inCSV: Array[String] =
    files.zipWithIndex map { case (file, i) =>   // TODO should be a flatmap
      Source.fromFile(file).getLines()
        .filter(_ startsWith "KPX")
        .map(i + _.replaceFirst("KPX", "").replaceAll(" ", ","))
        .toList
    } flatten

  // PairInfo : gives a unique id and a seen-count to each letter pair
  case class PairInfo(id: Int, var count: Int) { // case classiness allows for nice syntax later
    def asStringArray = Array(id.toString, count.toString)
  }


  /** outCSV : a csv file-string ready for ingestion by Mahout's FileDataModel constructor

    This algorithm is optimized for a single processor, but could use two mapreduce
    steps to accomplish the same thing with parallel mappers and reducers.
   */
  val letterMap = mutable.Map.empty[String, PairInfo]
  val outCSV: String = inCSV.map { line =>

    // get the letter pair
    val lineArray = line split ","
    val pair = lineArray.slice(1, 3).mkString(",")

    // make sure there's an ID for this letter pair
    if (letterMap contains pair) letterMap(pair).count += 1
    else letterMap(pair) = PairInfo(letterMap.size, 1)

    // output as triplet (user, item, value) for mahout
    List(lineArray(0), letterMap(pair).id, lineArray(3)).mkString(",")
  } mkString "\n"

  val modelDataWriter = new PrintWriter("OutFileData.csv")
  modelDataWriter write outCSV
  modelDataWriter.close()


  /** histogram : sorted csv histogram of letter pairs

     "highest,pair,count,id
      nextHighest,pair,count,id
      ... "
    */
  val histogramCSV: String = letterMap.map { case (pair, info) =>
    (pair split ",") ++ info.asStringArray        // put in [letter,letter,count,id] format
  }.toList
    .sortWith( _(2).toInt > _(2).toInt )          // sort by count, descending
    .map(_ mkString ",") mkString "\n"            // turn to CSV string

  println(histogramCSV)                           // print to console

  val header = "FirstLetter,SecondLetter,Count,ID\n"
  val histogramWriter = new PrintWriter("KerningHistogram.csv")
  histogramWriter write header + histogramCSV     // write to file
  histogramWriter.close()
}
