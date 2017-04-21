package gd.inu.storedqf

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by henry on 4/17/17.
  */
trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>
  override protected def afterAll() {
    super.afterAll()
    system.terminate()
    Await.result(system.whenTerminated, Duration.Inf)
  }
}
