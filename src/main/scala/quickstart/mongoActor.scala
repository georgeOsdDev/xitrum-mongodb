package quickstart

import xitrum.Server
import akka.actor.{Actor, ActorRef, Props}
import scala.collection.immutable.HashMap
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient

case class ReqFind(query: MongoDBObject)
case class ReqInsert(doc: MongoDBObject)
case class ReqUpdate(query: MongoDBObject, doc: MongoDBObject)
case class ReqRemove(query: MongoDBObject)

object MongoManager {
  val collectionARef = xitrum.Config.actorSystem.actorOf(mongoActor.props("collectionA"))
  val collectionBRef = xitrum.Config.actorSystem.actorOf(mongoActor.props("collectionB"))

  def get(ref:String) :ActorRef = {
    ref match {
      case "collectionA" => collectionARef
      case "collectionB" => collectionBRef
    }
  }

  val mongoClient = MongoClient("localhost", 27017)
  def getDB() ={
    mongoClient("test")
  }
}
object mongoActor {
  def props(name: String): Props = Props(classOf[mongoActor], name)
}
class mongoActor(name: String) extends Actor {
  def receive = {
    case "findAll" =>
      sender ! MongoManager.getDB()(name).find(MongoDBObject.empty)
    case find: ReqFind =>
      sender ! MongoManager.getDB()(name).find(find.query)
    case insert: ReqInsert =>
      sender ! MongoManager.getDB()(name).insert(insert.doc)
    case update: ReqUpdate =>
      sender ! MongoManager.getDB()(name).update(update.query,update.doc)
    case remove: ReqRemove =>
      sender ! MongoManager.getDB()(name).remove(remove.query)
  }
}