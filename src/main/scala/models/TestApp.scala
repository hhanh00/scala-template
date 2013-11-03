package models

import akka.event.Logging
import akka.actor._
import org.slf4j.LoggerFactory
import ch.qos.logback.classic._
import ch.qos.logback.classic.joran.JoranConfigurator
import com.typesafe.config.ConfigFactory

class TestApp extends FSM[TestApp.State, Null] {
  import TestApp._
  startWith(Idle, null)
  
  when(Idle) {
    case Event(Start, _) => goto(Running)
  }
  when(Running) {
    case Event(s: String, _) => 
      val reply = s"Hello $s"
      log.debug(reply)
      stay replying reply
    case Event(Stop, _) => 
      stop
  }
  
  onTransition {
    case Idle -> Running => log.info("Starting")
  }

  onTermination {
    case StopEvent(FSM.Normal, _, _) => log.info("Stopping")
  }
  
  initialize()
}


object TestApp extends App {
  trait State
  case object Idle extends State
  case object Running extends State
  
  case object Start
  case object Stop
  
  val context = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  val configurator = new JoranConfigurator
  configurator.setContext(context)
  context.reset()
  configurator.doConfigure("conf/logback.xml")
  val config = ConfigFactory.load("conf/application")
}