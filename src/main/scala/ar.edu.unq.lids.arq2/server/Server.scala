package ar.edu.unq.lids.arq2.server

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2._
import ar.edu.unq.lids.arq2.controllers.ResourceController
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import org.clapper.classutil.ClassFinder

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
      .filter[CommonFilters]
      controllers.foreach(controller => router.add(controller.newInstance()))
  }

  def controllers = {
    val finder = ClassFinder()
    val resource = classOf[ResourceController[_,_]]
    ClassFinder.concreteSubclasses(resource.getName, finder.getClasses.toIterator).map(clazz=>{
      Class.forName(clazz.name).asInstanceOf[Class[ResourceController[_,_]]]
    })
  }
}