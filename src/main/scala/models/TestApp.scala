package models

import akka.event.Logging
import akka.actor._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic._
import ch.qos.logback.classic.joran.JoranConfigurator
import com.typesafe.config.ConfigFactory

class TestApp extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case s: String => log.debug(s"Hello $s")
  }
}

object TestApp extends App {
  val context = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  val configurator = new JoranConfigurator
  configurator.setContext(context)
  context.reset()
  configurator.doConfigure("conf/logback.xml")
  val config = ConfigFactory.load("conf/application")
  
  val system = ActorSystem("TestApp", config)
  val testApp = system.actorOf(Props(classOf[TestApp]))
  
  testApp ! "world"
}