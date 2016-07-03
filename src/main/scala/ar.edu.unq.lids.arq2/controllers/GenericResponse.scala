package ar.edu.unq.lids.arq2.controllers

import com.google.gson.Gson

import scala.collection.mutable.Map
import scala.language.dynamics

case class GenericResponse(val properties:Map[String, Any] = Map()) extends Dynamic {

  def selectDynamic(name:String) = properties.get(name).getOrElse("")
  def updateDynamic[T <: AnyVal](name:String)(value:T) = properties(name) = value.asInstanceOf[Object]

  def toJson = new Gson().toJson(properties)

}
