package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2.exceptions.DuplicateResourceException
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

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


case class ListResult[T](items:List[T], paging:Paging)
case class Paging(limit:Int, offset:Int, total:Int)
