package ar.edu.unq.lids.arq2.persistence

import java.io.Serializable

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.{Criteria, StatementSelectValue}

class Repository[T<:Resource](implicit m: Manifest[T]) {

  def save(t:T) = t

  def alll = all[T]

  def get(ff:(T)=>String, value:StatementSelectValue): T = {
      query {
        (t: T) => {
          where(ff(t) :== value) select (t)
        }
      }.head
  }

  def filter(filters:List[((T)=>String, Option[String])]): List[T] = {
    select[T] where( t => filter(t, filters) )
  }

  protected def filter(t:T, filters:List[((T)=>String, Option[String])])={
    val criterias = filters.collect{ case (property, Some(value)) => property(t) :== value}
    criterias.reduce[Criteria]((c1, c2) => (c1 :&& c2).asInstanceOf[Criteria])
  }

}
