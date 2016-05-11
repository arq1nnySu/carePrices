package ar.edu.unq.lids.arq2.controllers

import scala.collection.mutable.Map
import scala.language.dynamics

case class GenericResponse(val properties:Map[String, AnyVal] = Map()) extends Dynamic {

  def selectDynamic(name:String) = properties.get(name).getOrElse("")
  def updateDynamic[T <: AnyVal](name:String)(value:T) = properties(name) = value

  def toJson = properties

}
