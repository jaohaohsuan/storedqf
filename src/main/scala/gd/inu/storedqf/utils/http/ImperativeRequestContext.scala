package gd.inu.storedqf.utils.http

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}

import scala.concurrent.Promise

/**
  * Created by henry on 4/20/17.
  */

final class ImperativeRequestContext(ctx: RequestContext, promise: Promise[RouteResult]) {
  private implicit val ec = ctx.executionContext
  def response(inner: Route): Unit = inner(ctx).onComplete(promise.complete)
  def complete(obj: ToResponseMarshallable): Unit = ctx.complete(obj).onComplete(promise.complete)
  def fail(error: Throwable): Unit = ctx.fail(error).onComplete(promise.complete)
}
