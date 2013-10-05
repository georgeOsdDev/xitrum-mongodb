package quickstart

import akka.actor.{Actor, ActorRef, Props}

import com.mongodb.casbah.MongoCursor
import com.mongodb.casbah.Imports._

class Data extends Actor {
  private var collectionA = Seq[Object]()
  def init ={
    println("init")
    val colA = MongoManager.get("collectionA")
    colA ! "findAll"
    context.become {
      case result: MongoCursor =>
          for(d <- result) print(d)
          collectionA = result.toSeq +: collectionA
          context.unbecome()
    }
  }
  def receive = {
    case "init" => {
      init
    }
    case "print" => {
      println("print")
      for(doc <- collectionA) println(doc)
    }
    case "insert" => {
      val colB = MongoManager.get("collectionB")
      val a = MongoDBObject("hello" -> "world")
      colB ! ReqInsert(a)
    }
  }
}