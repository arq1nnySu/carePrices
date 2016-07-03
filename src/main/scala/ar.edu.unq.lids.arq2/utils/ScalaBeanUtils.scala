package ar.edu.unq.lids.arq2.utils

import java.beans.{BeanInfo, Introspector, PropertyDescriptor}
import java.lang.reflect.Method
import java.util.{Comparator, TreeMap}

import ar.edu.unq.lids.arq2.service.Resource
import org.apache.commons.beanutils.PropertyUtilsBean

import scala.util.Try


object ScalaBeanUtils {
  private val SCALA_SETTER_SUFFIX = "_$eq"
  val propertyBean = new PropertyUtilsBean

  def copy(source: Object, target: Object): Unit = {
    val pdSource = getPropertyDescriptors(Introspector.getBeanInfo(source.getClass))
    val pdTarget = getPropertyDescriptors(Introspector.getBeanInfo(target.getClass))

    for {
      sourceDescriptor <- pdSource
      value <- Try(sourceDescriptor.getReadMethod()).toOption
      targetDescriptor <- pdTarget.find(_.getName == sourceDescriptor.getName)
    } yield {
      copy(target, value.invoke(source), Option(targetDescriptor))
    }

  }

  def copy(target:Object, value:Object, propertyDescriptor: Option[PropertyDescriptor] ) ={
    propertyDescriptor match {
      case None => {}
      case Some(descriptor) => {
        value match{
          case o:Option[Object] => {
            if(descriptor.getWriteMethod != null) descriptor.getWriteMethod.invoke(target, o.getOrElse(null))
          }
          case null => {}
          case x if descriptor.getPropertyType.isAssignableFrom(classOf[Option[_]]) => descriptor.getWriteMethod.invoke(target, Option(x))
          case x:Resource => descriptor.getWriteMethod.invoke(target, x.id)
          case x => Option(descriptor.getWriteMethod).map(_.invoke(target, x))
        }
      }
    }
    target
  }

  def getPropertyDescriptor(descriptors: Array[PropertyDescriptor], property: String): PropertyDescriptor = {
    var propertyName = property
    if (property.startsWith("_")) {
      propertyName = propertyName.substring(1)
    }
    for (descriptor <- descriptors if descriptor.getName == propertyName) {
      return descriptor
    }
    null
  }

  def getPropertyDescriptors(target: Object): Array[PropertyDescriptor] = {
    getPropertyDescriptors(Introspector.getBeanInfo(target.getClass))
  }

  def getPropertyDescriptors(beanInfo: BeanInfo): Array[PropertyDescriptor] = {
    val propertyDescriptors = new TreeMap[String, PropertyDescriptor](new PropertyNameComparator())
    for (pd <- beanInfo.getPropertyDescriptors) {
      propertyDescriptors.put(pd.getName, pd)
    }
    for (md <- beanInfo.getMethodDescriptors) {
      val method = md.getMethod
      if (isObjectMethod(method)) {
        //continue
      }
      if (isScalaSetter(method)) {
        addScalaSetter(propertyDescriptors, method)
      } else if (isScalaGetter(method)) {
        addScalaGetter(propertyDescriptors, method)
      }
    }
    propertyDescriptors.values.toArray(Array.ofDim[PropertyDescriptor](propertyDescriptors.size))
  }

  private def isScalaGetter(method: Method): Boolean = {
    method.getParameterTypes.length == 0 && method.getReturnType != Void.TYPE &&
      !(method.getName.startsWith("get") || method.getName.startsWith("is"))
  }

  def isScalaSetter(method: Method): Boolean = {
    method.getParameterTypes.length == 1 && method.getReturnType == Void.TYPE &&
      method.getName.endsWith(SCALA_SETTER_SUFFIX)
  }

  private def addScalaSetter(propertyDescriptors: java.util.Map[String, PropertyDescriptor], writeMethod: Method) {
    val propertyName = writeMethod.getName.substring(0, writeMethod.getName.length - SCALA_SETTER_SUFFIX.length)
    var pd = propertyDescriptors.get(propertyName)
    if (pd != null && pd.getWriteMethod == null) {
      pd.setWriteMethod(writeMethod)
    } else if (pd == null) {
      pd = new PropertyDescriptor(propertyName, null, writeMethod)
      propertyDescriptors.put(propertyName, pd)
    }
  }


  private def addScalaGetter(propertyDescriptors: java.util.Map[String, PropertyDescriptor], readMethod: Method) {
    val propertyName = readMethod.getName
    var pd = propertyDescriptors.get(propertyName)
    if (pd != null && pd.getReadMethod == null) {
      pd.setReadMethod(readMethod)
    } else if (pd == null) {
      pd = new PropertyDescriptor(propertyName, readMethod, null)
      propertyDescriptors.put(propertyName, pd)
    }
  }

  def isObjectMethod(method: Method): Boolean = {
    Try(
      classOf[Object].getDeclaredMethod(method.getName) != null
    ).getOrElse(false)
  }

}

private class PropertyNameComparator extends Comparator[String] {

  def compare(left: String, right: String): Int = {
    val leftBytes = left.getBytes
    val rightBytes = right.getBytes
    for (i <- 0 until left.length) {
      if (right.length == i) {
        return 1
      }
      val result = leftBytes(i) - rightBytes(i)
      if (result != 0) {
        return result
      }
    }
    left.length - right.length
  }
}
