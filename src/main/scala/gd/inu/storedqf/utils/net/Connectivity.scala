package gd.inu.storedqf.utils.net

import akka.http.scaladsl.Http.ServerBinding

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by henry on 4/17/17.
  */
object Connectivity {

  implicit class httpServerBinding(binding: ServerBinding) {

    private val ServerBinding(addr) = binding

    def port: Int = addr.getPort
    def ip: String = addr.getHostString

    def unbinding()(implicit executor: ExecutionContext): Future[(String, Int)] = binding.unbind().map { _ => (ip, port) }
  }
}
