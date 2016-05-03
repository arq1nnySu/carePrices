package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2.controllers.{ListResult, Paging}
import ar.edu.unq.lids.arq2.persistence.{QueryResult, Repository}
import net.fwbrasil.activate.statement.StatementSelectValue

class ResourceService[T<:Resource, D<:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]){
  type Dto = DTO[T]
  var repository = new Repository[T]

  implicit def dtoToResource(dto:D) = dto.toResource()
  implicit def resourceToDTO(t:T):D = t.toData(manifestD.runtimeClass.asInstanceOf[Class[Dto]]).asInstanceOf[D]
  implicit def resourcesToDTO(ts:List[T]):List[D] = ts.map(resourceToDTO(_))

  def save(data:D):Dto = transactional{
    repository.save(data)
  }

  def all(limit:Option[Int], offset:Option[Int]) = transactional{
    paginateResult(repository.all(limit, offset))

  }

  def get(ff:(T)=>String, value:StatementSelectValue):Dto = transactional{
    repository.get(ff, value)
  }

  def filter(limit:Option[Int], offset:Option[Int], filters:List[((T)=>String, Option[String])]) = transactional{
    paginateResult(repository.filter(filters, limit, offset))
  }

  def paginateResult(result:QueryResult[T]) = {
    ListResult(result.items.map(resourceToDTO(_)), Paging(result.limit, result.offset, result.total))
  }

}
