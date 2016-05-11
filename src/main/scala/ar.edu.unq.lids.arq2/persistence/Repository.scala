package ar.edu.unq.lids.arq2.persistence

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.configuration
import ar.edu.unq.lids.arq2.controllers.{Paging, ListResult}
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.query.{Query, PaginationNavigator}
import net.fwbrasil.activate.statement.{BooleanOperatorCriteria, Where, Criteria, StatementSelectValue}

class Repository[T<:Resource](implicit m: Manifest[T]) {

  def save(t:T) = t

  def all(limit:Option[Int]=None, offset:Option[Int]=None) = filter[T](List(), limit, offset)

  def get(ff:(T)=>String, value:StatementSelectValue): T = {
    dynamicQuery {
        (t: T) => {
          where(ff(t) :== value) select (t)
        }
      }.head
  }

  def filter(restriction: Restriction[T])  ={
    dynamicQuery {
      (t: T) => {
        Where(restriction.build(t)) select (t)
      }
    }
  }

  def filter[X](filters:List[((T)=>String, Option[String])], limit:Option[Int]=None, offset:Option[Int]=None, selectValue:(T)=>X = (t:T)=>t.asInstanceOf[X])(implicit tval1: (=> X) => StatementSelectValue):QueryResult[X] = {
    val queryLimit = limit.getOrElse(configuration.response.limitSize).min(configuration.response.maxLimitSize)
    val queryOffset = offset.getOrElse(0)

    val commonWhere = (t:T) => Where(filter(t, filters))

    val total = dynamicQuery {(t:T) => commonWhere(t) select(1) orderBy(t.id)}.size
    val items = dynamicQuery {
      (t:T) => { commonWhere(t) select(selectValue(t)) orderBy(t.id) limit(queryLimit) offset(queryOffset) }
    }

    QueryResult( items, queryLimit, queryOffset, total)
  }

  def filter(t:T, filters:List[((T)=>String, Option[String])]):Option[Criteria] ={
    val criterias = filters.collect{ case (property, Some(value)) => property(t) :== value}
    if(criterias.isEmpty) {
      None
    }else {
      Some(criterias.reduce[Criteria]((c1, c2) => (c1 :&&c2).asInstanceOf[Criteria]))
    }
  }

}


case class QueryResult[T](items:List[T], limit:Int, offset:Int, total:Int)
