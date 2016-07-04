package ar.edu.unq.lids.arq2.controllers

import javax.inject.{Singleton, Inject}

import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2.exceptions.{InvalidRequest, DuplicateResourceException}
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Status, Response, Request}
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.internal.exceptions.ExceptionManager
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.{Return, ConstFuture, Throw, Future}
import scala.collection.mutable.Map

import scala.util.{Failure, Success, Try}

abstract class ResourceController[T <: Resource, D <:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]) extends Controller{
  val service = new ResourceService[T, D]{}
  lazy val cacheService  =  new CacheService

  def cache(request:Any) = cacheService.memorize(request.toString)_

  def all =  { request: ListRequest =>
    val result = cache(s"${request.request.path}:$request"){service.all(request.limit, request.offset)}
    response.ok.json(result)
  }

  def save(endpoint:String) =  { dto: D =>
    Try(service.save(dto)) match {
      case Success(null) => response.notFound
      case Success(resource) => response.created.location(s"/$endpoint/${resource.id.getOrElse("")}")
      case Failure(DuplicateResourceException(resource)) => response.conflict.location(s"/$endpoint/${resource.id}")
      case Failure(x) => throw x
    }

  }

  def byId =  { request: Request =>
    val id = request.params.getOrElse("id", "")
    response.ok.json(service.get(_.id, id))
  }
}

@Singleton
class ExceptionHandlerFilter @Inject()( exceptionManager: ExceptionManager, response: ResponseBuilder)
  extends SimpleFilter[Request, Response] {

  override def apply(request: Request, service: Service[Request, Response]) = {
    service(request).poll match{
      case Some(Return(r)) => Future.value(r)
      case Some(Throw(x:NoSuchMethodException)) => response.notFound.json(new GenericResponse(Map("route" -> request.path, "error" -> "notFound")).toJson).toFuture
      case Some(Throw(x:InvalidRequest)) => response.badRequest.json(new GenericResponse(Map("error" -> "badRequest")).toJson).toFuture
      case Some(Throw(x)) => Future.value(exceptionManager.toResponse(request, x))
    }
  }
}


case class ListResult[T](items:java.util.List[T], paging:Paging)
case class Paging(limit:Int, offset:Int, total:Int)
