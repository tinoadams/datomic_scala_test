import datomic.Peer
import java.util.UUID
import scala.collection.JavaConversions._

object Hello {

  def main(args: Array[String]): Unit = {
    val uri = "datomic:mem://seattle"
    Peer.createDatabase(uri)
    val conn = Peer.connect(uri)

    //
    // Create a schema for our assets
    //
    val assetIdAttr: java.util.Map[_, _] = Map(
      ":db/id" -> Peer.tempid(":db.part/db"),
      ":db/ident" -> ":asset/id",
      ":db/valueType" -> ":db.type/string",
      ":db/cardinality" -> ":db.cardinality/one",
      ":db/unique" -> ":db.unique/identity",
      ":db/doc" -> "An asset's unique id",
      ":db.install/_attribute" -> ":db.part/db"
    )
    val assetTitleAttr: java.util.Map[_, _] = Map(
      ":db/id" -> Peer.tempid(":db.part/db"),
      ":db/ident" -> ":asset/title",
      ":db/valueType" -> ":db.type/string",
      ":db/cardinality" -> ":db.cardinality/one",
      ":db/doc" -> "An asset's title",
      ":db.install/_attribute" -> ":db.part/db"
    )
    val schema = List(assetIdAttr, assetTitleAttr)
    conn.transact(schema).get()

    //
    // Create a partition for our assets
    //
    val partitionName = ":db.part/assets"
    val partition: java.util.Map[_, _] = Map(
      ":db/id" -> Peer.tempid(":db.part/db"),
      ":db/ident" -> partitionName,
      ":db.install/_partition" -> ":db.part/db"
    )
    conn.transact(List(partition)).get()

    //
    // Insert some assets
    //
    val assetId1 = UUID.randomUUID().toString
    val asset1: java.util.Map[_, _] = Map(
      ":db/id" -> Peer.tempid(partitionName),
      ":asset/id" -> assetId1,
      ":asset/title" -> "Asset number 1"
    )
    conn.transact(List(asset1)).get()

    val assetId2 = UUID.randomUUID().toString
    val asset2: java.util.Map[_, _] = Map(
      ":db/id" -> Peer.tempid(partitionName),
      ":asset/id" -> assetId2,
      ":asset/title" -> "Asset number 2"
    )
    conn.transact(List(asset2)).get()

    //
    // Query the db and retrieve all assets
    //
    println("---- All assets ----")
    val results1 = Peer.q("[:find ?a :where [?a :asset/id]]", conn.db())
    for (asset <- results1) printAsset(conn, asset.get(0))

    //
    // Query the db and retrieve the asset by its id
    //
    println(s"---- Assets with id ${assetId1} ----")
    val results2 = Peer.q("[:find ?a :in $ ?id :where [?a :asset/id ?id]]", conn.db(), assetId1)
    for (asset <- results2) printAsset(conn, asset.get(0))
  }

  def printAsset(conn: datomic.Connection, entityId: Object) {
    val entity = conn.db().entity(entityId)
    println(entityId + ": " + entity.get(":asset/title"))
  }
}
