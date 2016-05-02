package ar.edu.unq.lids.arq2.persistence

import ar.edu.unq.lids.arq2.model.Shop
import ar.edu.unq.lids.arq2.service.Resource
import net.fwbrasil.activate.statement.Criteria
import ar.edu.unq.lids.arq2.CartePriceActivateContext._

//TODO esto esta en alpha

trait RestrictionType {
  def apply[T<:Resource](restriction: SimpleRestriction[T], t:T):Option[Criteria]
}

object EqRestrictionType extends RestrictionType{
  def apply[T<:Resource](restriction: SimpleRestriction[T], t:T) = {
    restriction.restriction match {
      case (property, Some(value)) => Some(property(t) :== value)
      case _ => None
    }
  }
}

trait Restrictionable[T<:Resource]{

  implicit def toRestriction(property:(T)=>String) = Property[T](property)

}

case class Property[T<:Resource](property:(T)=>String){
  def =:=(value:Option[String]):SimpleRestriction[T] = SimpleRestriction[T]((property, value), EqRestrictionType)
  def =:=(value:String):SimpleRestriction[T] = =:=(Option(value))
}

trait Restriction[T<: Resource]{

  def and(r1:Restriction[T], r2:Restriction[T]) = CompositeRestriction[T](r1, r2, AndOperator)
  def or(r1:Restriction[T], r2:Restriction[T]) = CompositeRestriction[T](r1, r2, OrOperator)

  def build(t:T):Option[Criteria]
}

case class SimpleRestriction[T<: Resource](restriction:((T)=>String, Option[String]), restrictionType:RestrictionType) extends Restriction[T]{

  def build(t:T) = restrictionType(this, t)
}

case class CompositeRestriction[T<: Resource](r1: Restriction[T], r2: Restriction[T], operator:Operator) extends Restriction[T]{

  def build(t:T) = operator(r1.build(t), r2.build(t))
}


trait  Operator{
  def apply(op1:Option[Criteria], op2:Option[Criteria]):Option[Criteria]
}

object AndOperator extends Operator{
  def apply(op1:Option[Criteria], op2:Option[Criteria])  ={
    (op1, op2) match {
      case (None, None) => None
      case (x, None) => x
      case (None, x) => x
      case (Some(c1), Some(c2)) => Some(c1 :&& c2)
    }
  }
}

object OrOperator extends Operator{
  def apply(op1:Option[Criteria], op2:Option[Criteria])  ={
    (op1, op2) match {
      case (None, None) => None
      case (x, None) => x
      case (None, x) => x
      case (Some(c1), Some(c2)) => Some(c1 :|| c2)
    }
  }
}

case class FilterCriteria(criteria:Criteria) {

  def and(aCriteria:Criteria) = FilterCriteria(criteria :&& criteria)
  def or(aCriteria:Criteria) = FilterCriteria(criteria :|| criteria)
}
