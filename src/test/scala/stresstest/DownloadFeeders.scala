package stresstest

import java.io.{File, PrintWriter}

import ar.edu.unq.lids.arq2.controllers.ListResult
import ar.edu.unq.lids.arq2.model.{Product, Shop}
import ar.edu.unq.lids.arq2.utils.{ScalaBeanUtils, Serializer}
import com.google.common.reflect.TypeToken

import scala.collection.JavaConversions._
import scalaj.http._

object DownloadFeeders extends App{

  class Interval(val start: Long, val end: Long)

  def loadFeeders(): Unit ={
    val productType = new TypeToken[ListResult[Product]](){}
    val shopType = new TypeToken[ListResult[Shop]](){}
    loadEntity[Product](List("id", "name", "barcode"), "product", productType)
    loadEntity[Shop](List("id", "name", "latitude", "longitude", "address", "location", "chain"), "shop", shopType)
  }

  def loadEntity[T<:Object](properties:List[String], entity:String, typeToken:TypeToken[ListResult[T]]): Unit ={
    val service = entity+"s"
    val response = Http(TestConfig.fullHost+service).param("limit",TestConfig.limit).asString
    val result:ListResult[T] = Serializer.gson.fromJson(response.body, typeToken.getType)

    var csv = properties.map(entity+"_"+_).mkString(",") +"\n"
    csv += result.items.map(obj =>{
      val info = ScalaBeanUtils.getPropertyDescriptors(obj)
      properties.map( property=>{
        val descriptor = ScalaBeanUtils.getPropertyDescriptor(info, property)
        descriptor.getReadMethod.invoke(obj)
      })
    }.mkString(",")
    ).mkString("\n")
    val pw = new PrintWriter(new File(s"src/test/resources/$service.csv" ))
    pw.write(csv)
    pw.close
    println(s"Done ${service}.scv")
  }

}
