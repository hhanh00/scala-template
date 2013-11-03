package models

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import akka.testkit._
import akka.actor._
import org.scalatest._
import org.scalatest.matchers.MustMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestAppTest(system: ActorSystem) extends TestKit(system) with fixture.WordSpecLike with ImplicitSender with MustMatchers with BeforeAndAfterAll {
  def this() = this(ActorSystem())
  
  type FixtureParam = ActorRef
  override def afterAll() = TestKit.shutdownActorSystem(system)
  
  def withFixture(test: OneArgTest) {
    val testApp = system.actorOf(Props(classOf[TestApp]))
    testApp ! TestApp.Start
    test(testApp)
    testApp ! TestApp.Stop
  }
  
  "A test actor" must {
    "say hello to people" in { f =>
      f ! "hanh"
      expectMsg("Hello hanh")
    }
  }
}