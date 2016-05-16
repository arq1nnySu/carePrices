package ar.edu.unq.lids.arq2.service

import akka.actor.Status.Success
import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2.controllers.{ListResult, Paging}
import ar.edu.unq.lids.arq2.exceptions.{InvalidRequest}
import ar.edu.unq.lids.arq2.persistence.{QueryResult, Repository}
import com.twitter.util.Try
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

  def get(ff:(T)=>String, value:StatementSelectValue):Dto = getResource(ff, value)

  def getResource(ff:(T)=>String, value:StatementSelectValue):T = transactional{
    repository.get(ff, value) match{
      case t:T => t
      case null => throw new InvalidRequest()
    }
  }

  def paginatefilter(limit:Option[Int], offset:Option[Int], filters:List[((T)=>String, Option[String])]) = transactional{
    paginateResult(filter[T](limit, offset, filters))
  }

  def filter[X](limit:Option[Int], offset:Option[Int], filters:List[((T)=>String, Option[String])], selectValue:(T)=>X = (t:T)=>t.asInstanceOf[X])(implicit tval1: (=> X) => StatementSelectValue) = transactional{
    repository.filter(filters, limit, offset, selectValue)
  }

  def paginateResult(result:QueryResult[T]) = {
    ListResult(result.items.map(resourceToDTO(_)), Paging(result.limit, result.offset, result.total))
  }

}
