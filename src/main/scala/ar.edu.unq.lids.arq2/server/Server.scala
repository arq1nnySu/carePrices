package ar.edu.unq.lids.arq2.server

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.controllers.ProductController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import scala.util.Properties


trait AppController extends Controller

object Server extends HttpServer {

  transactional{

  }

  override val defaultFinatraHttpPort = ":" + Properties.envOrElse("PORT", "8081")

  override val disableAdminHttpServer = true

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[ProductController]
  }


}