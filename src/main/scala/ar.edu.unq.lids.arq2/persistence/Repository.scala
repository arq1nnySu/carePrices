package ar.edu.unq.lids.arq2.persistence

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.StatementSelectValue

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

}