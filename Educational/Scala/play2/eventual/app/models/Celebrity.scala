package models

import play.api.libs.json.Json
import models.Name.NameBSONReader
import models.Name.NameBSONWriter
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

/*
 * Author: Sari Haj Hussein
 */

case class Celebrity(id: Option[BSONObjectID], name: Name, website: String, bio: String)

object Celebrity {
  /** serialize/Deserialize a Celebrity into/from JSON value */
  implicit val celebrityFormat = Json.format[Celebrity]

  /** serialize a Celebrity into a BSON */
  implicit object CelebrityBSONWriter extends BSONDocumentWriter[Celebrity] {
    def write(celebrity: Celebrity): BSONDocument =
      BSONDocument(
        "_id" -> celebrity.id.getOrElse(BSONObjectID.generate),
        "name" -> celebrity.name,
        "website" -> celebrity.website,
        "bio" -> celebrity.bio)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object CelebrityBSONReader extends BSONDocumentReader[Celebrity] {
    def read(doc: BSONDocument): Celebrity =
      Celebrity(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Name]("name").get,
        doc.getAs[String]("website").get,
        doc.getAs[String]("bio").get)
  }
}