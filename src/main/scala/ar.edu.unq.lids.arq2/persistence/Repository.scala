package ar.edu.unq.lids.arq2.persistence

import ar.edu.unq.lids.arq2.CartePriceActivateContext
import CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.StatementSelectValue

import scala.reflect.ClassTag

class Repository[T<:Resource: ClassTag](implicit m: Manifest[T]) {

  def save(t:T): T ={
      t
  }

  def alll = all[T]

  def get(ff:(T)=>String, value:StatementSelectValue): T = {
      query {
        (t: T) => {
          where(ff(t) :== value) select (t)
        }
      }.head
  }

}