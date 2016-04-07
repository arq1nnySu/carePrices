package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.model.Product
import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.persistence.Repository
import net.fwbrasil.activate.statement.StatementSelectValue


class ResourceService[T<:Resource, D<:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]){
  type Dto = DTO[T]
  var repository = new Repository[T]

  implicit def dtoToResource(dto:D) = dto.toResource()
  implicit def resourceToDTO(t:T):Dto = t.toData(manifestD.runtimeClass.asInstanceOf[Class[Dto]])

  def save(data:D):Dto = transactional{
    repository.save(data)
  }

  def all = transactional{
    repository.alll.map(resourceToDTO(_))
  }

  def get(ff:(T)=>String, value:StatementSelectValue):Dto = transactional{
    repository.get(ff, value)
  }

}

class ProductService extends ResourceService[Product, ProductDTO]{}
//class PriceService extends ResourceService[Price, ProductDTO]{}
