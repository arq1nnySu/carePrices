package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

abstract class ResourceController[T <: Resource, D <:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]) extends Controller{
  val service = new ResourceService[T, D]{}
  lazy val cacheService  =  new CacheService

  def all =  { request: Request =>
    response.ok.json(service.all)
  }

  def save(endpoint:String) =  { dto: D =>
    val resource = service.save(dto)
    response.created.location(s"/$endpoint/${resource.id.getOrElse("")}")
  }

  def byId =  { request: Request =>
    val id = request.params.getOrElse("id", "")
    response.ok.json(service.get(_.id, id))
  }

}
