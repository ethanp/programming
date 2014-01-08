package models

import play.api.libs.json.Json
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer

/*
 * Author: Sari Haj Hussein
 */

case class Name(first: String, last: String)

object Name {
  /** serialize/deserialize a Name into/from JSON value */
  implicit val nameFormat = Json.format[Name]

  /** serialize a Name into a BSON */
  implicit object NameBSONWriter extends BSONDocumentWriter[Name] {
    def write(name: Name): BSONDocument =
      BSONDocument(
        "first" -> name.first,
        "last" -> name.last)
  }

  /** deserialize a Name from a BSON */
  implicit object NameBSONReader extends BSONDocumentReader[Name] {
    def read(doc: BSONDocument): Name =
      Name(
        doc.getAs[String]("first").get,
        doc.getAs[String]("last").get)
  }
}