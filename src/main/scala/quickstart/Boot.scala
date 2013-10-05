package quickstart

import akka.actor.{Actor, ActorRef, Props}
import xitrum.annotation.GET
import xitrum.{Action,ActionActor,Server}
import com.mongodb.DBObject

object Boot {
  val data = xitrum.Config.actorSystem.actorOf(Props(classOf[Data]))
  def main(args: Array[String]) {
    data ! "init"
    Server.start()
  }
  def getData():ActorRef = {
    data
  }
}

@GET("/")
class SiteIndex extends ActionActor{
  override def execute(){
    val data = Boot.getData()
    data ! "print"
    data ! "insert"
    respondHtml("ok")
  }
}