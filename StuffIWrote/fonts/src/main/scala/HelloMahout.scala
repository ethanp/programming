import java.io.{PrintWriter, File}
import java.util
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.{Recommender, RecommendedItem}
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import scala.io.Source


/**
 * Base Source:
 * fady64
 * https://github.com/fady64/com.rukbysoft.examples.mahout/blob/MahoutRecommender/
 * src/main/java/com/rukbysoft/examples/mahout/recommender/MovieRecommender.java
 */

object A extends App {

  val model: DataModel = new FileDataModel(new File(
    "/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/fonts/src/main/scala/MoviesList.csv"

    // Doesn't work yet because it's expecting (usr,mov,val) but I'm giving (usr,pt1,pt2,val)
//    "/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/fonts/OutFileData.csv"
  ))

  val similarity: UserSimilarity = new PearsonCorrelationSimilarity(model)
  val neighborhood: UserNeighborhood = new NearestNUserNeighborhood(2, similarity, model)
  val recommender: Recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)
  import scala.collection.JavaConversions._
  val recommendations: util.List[RecommendedItem] = recommender.recommend(1, 2)
  recommendations foreach println
}

/**
 * process the raw files into something suitable for mahout
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
