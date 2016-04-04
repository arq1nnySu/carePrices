package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.model.Product

//import ar.edu.unq.lids.arq2.model.Product
import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.persistence.Repository

import scala.reflect.ClassTag

class ResourceService[T<:Resource: ClassTag, D<:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]){

  var repository = new Repository[T]

  implicit def dtoToResource(dto:D) = dto.toResource()
  implicit def resourceToDTO(t:T) = t.toData(manifestD.runtimeClass.asInstanceOf[Class[DTO[T]]])

  def save(data:D) = transactional{
    resourceToDTO(repository.save(data))
  }

  def all = transactional{
    repository.alll.map(resourceToDTO(_))
  }

//  def get(id:String) = transactional{
//    repository.get(_.id, id)
//  }
//  def get(ff:(T)=>StatementSelectValue, value:StatementSelectValue) = transactional{
//    repository.get(ff, value)
//  }

}

class ProductService extends ResourceService[Product, ProductDTO]{}
