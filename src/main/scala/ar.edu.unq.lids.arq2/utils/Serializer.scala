package ar.edu.unq.lids.arq2.utils

import java.lang.reflect.Type

import com.google.gson._
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}

object Serializer {

  val DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC)

  lazy val gson = new GsonBuilder()
    .registerTypeHierarchyAdapter(classOf[Seq[Any]], new ListSerializer)
    .registerTypeHierarchyAdapter(classOf[Map[Any,Any]], new MapSerializer)
    .registerTypeHierarchyAdapter(classOf[Option[Any]], new OptionSerializer)
    .registerTypeAdapter(classOf[DateTime], new DateTimeSerializer)
    .create()

  class ListSerializer extends JsonSerializer[Seq[Any]] {
    override def serialize(src: Seq[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      import scala.collection.JavaConverters._
      context.serialize(src.toList.asJava)
    }
  }

  class MapSerializer extends JsonSerializer[Map[Any,Any]] {
    override def serialize(src: Map[Any,Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      import scala.collection.JavaConverters._
      context.serialize(src.asJava)
    }
  }

  class OptionSerializer extends JsonSerializer[Option[Any]] with JsonDeserializer[Option[Any]] {
    override def serialize(src: Option[Any], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      src match {
        case None => JsonNull.INSTANCE
        case Some(v) => context.serialize(v)
      }
    }
    def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext):Option[Any] = {
      if (json.isJsonNull) {
        None
      } else if (json.isJsonObject && json.getAsJsonObject.entrySet().size()==0) {
        None
      } else {
        Option(json.getAsString)
      }
    }
  }

  class DateTimeSerializer extends JsonSerializer[DateTime] {
    override def serialize(src: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
      new JsonPrimitive(DATE_TIME_FORMATTER.print(src))
    }
  }


}