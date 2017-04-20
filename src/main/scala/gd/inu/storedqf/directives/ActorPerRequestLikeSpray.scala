package gd.inu.storedqf.directives

import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import gd.inu.storedqf.utils.http.ImperativeRequestContext

import scala.concurrent.Promise

/**
  * Created by henry on 4/20/17.
  */
object ActorPerRequestLikeSpray {
  def imperativelyComplete(inner: ImperativeRequestContext => Unit): Route = { ctx: RequestContext =>
    val p = Promise[RouteResult]()
    inner(new ImperativeRequestContext(ctx, p))
    p.future
  }
}
