package ar.edu.unq.lids.arq2.persistence

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.configuration
import ar.edu.unq.lids.arq2.controllers.{Paging, ListResult}
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.query.{Query, PaginationNavigator}
import net.fwbrasil.activate.statement.{Where, Criteria, StatementSelectValue}

class Repository[T<:Resource](implicit m: Manifest[T]) {

  def save(t:T) = t

  def all(limit:Option[Int]=None, offset:Option[Int]=None) = filter(List(), limit, offset)

  def get(ff:(T)=>String, value:StatementSelectValue): T = {
      query {
        (t: T) => {
          where(ff(t) :== value) select (t)
        }
      }.head
  }

  def filter(filters:List[((T)=>String, Option[String])], limit:Option[Int]=None, offset:Option[Int]=None):QueryResult[T] = {
    val queryLimit = limit.getOrElse(configuration.server.listPageSize)
    val queryOffset = offset.getOrElse(0)

    val baseQuery = (t:T) => Where(filter(t, filters)) select(t) orderBy(t.id)

    val total = query {baseQuery}.size
    val items = query {
      (t:T) => { baseQuery(t) limit(queryLimit) offset(queryOffset) }
    }

    QueryResult( items, queryLimit, queryOffset, total)
  }

  protected def filter(t:T, filters:List[((T)=>String, Option[String])]):Option[Criteria] ={
    var criterias = filters.collect{ case (property, Some(value)) => property(t) :== value}
    if(criterias.isEmpty) {
      None
    }else {
      Some(criterias.reduce[Criteria]((c1, c2) => (c1 :&& c2).asInstanceOf[Criteria]))
    }
  }
}


case class QueryResult[T](items:List[T], limit:Int, offset:Int, total:Int)
