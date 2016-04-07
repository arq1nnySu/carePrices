package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class ResourceController[T <: Resource, D <:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]) extends Controller{
  val service = new ResourceService[T, D]{}

  def all =  { request: Request =>
    response.ok.json(service.all)
  }

  def save =  { dto: D =>
    response.ok.json(service.save(dto))
  }

  def byId =  { request: Request =>
    val id = request.params.getOrElse("id", "")
    response.ok.json(service.get(_.id, id))
  }

}
