package ar.edu.unq.lids.arq2.server

import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2._
import ar.edu.unq.lids.arq2.controllers._
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

object Server extends HttpServer {

  transactional{
    if(configuration.database.fillData) bootstrap.bootstrap.data
  }

  override val defaultFinatraHttpPort = configuration.server.port

  override val disableAdminHttpServer = true

  override def configureHttp(router: HttpRouter) {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[NewRelicFilter]
      .filter[ExceptionHandlerFilter]
      .add[ProductController]
      .add[ShopController]
      .add[PriceController]
      .add[ServerController]
  }
}