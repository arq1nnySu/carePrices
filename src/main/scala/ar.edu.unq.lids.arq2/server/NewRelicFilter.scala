package ar.edu.unq.lids.arq2.server

import java.util.Collections
import javax.inject.Singleton

import com.newrelic.api.agent.{Response => newrelicResponse, _}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.{Duration, Time}

import scala.collection.JavaConversions._

@Singleton
class NewRelicFilter extends SimpleFilter[Request, Response] {

  def apply(request:Request, service: Service[Request, Response]) = {
    val start = Time.now

    service(request)
      .onSuccess { r =>
        val duration = Time.now - start
        reportOk(request, r, duration)
      }
      .onFailure { e =>
        val duration = Time.now - start
        reportErr(request, duration, e)
      }
  }

  @Trace(dispatcher = true)
  def reportOk(request:Request, response: Response,  duration:Duration) {
    val uri = request.path
    NewRelic.setTransactionName(null, uri)
    NewRelic.setRequestAndResponse(Req(request), Resp(Some(response)))
    NewRelic.recordResponseTimeMetric(uri, duration.inMillis)
  }

  @Trace(dispatcher = true)
  def reportErr(request:Request, duration:Duration, e:Throwable) {
    val uri = request.path
    NewRelic.setTransactionName(null, uri)
    NewRelic.setRequestAndResponse(Req(request), Resp(None, Some(e)))
    NewRelic.noticeError(e)
    NewRelic.recordResponseTimeMetric(uri, duration.inMillis)
  }

  case class Req(request:Request) extends ExtendedRequest {
    def getRequestURI = request.path
    def getHeader(name: String) = request.headerMap(name)
    def getRemoteUser = null
    def getParameterNames = Collections.enumeration(request.params.keySet.toList)
    def getParameterValues(p1: String) = request.params.values.toArray
    def getAttribute(name: String) = null
    def getHeaderType() = HeaderType.HTTP
    def getCookieValue(name:String) = request.cookies(name).value
    def getMethod() = request.method.toString
  }

  case class Resp(response:Option[Response], e:Option[Throwable]=None) extends newrelicResponse {
    def getContentType = response.map(_.contentType.get).getOrElse("application/json")
    def getHeaderType = HeaderType.HTTP
    def setHeader(name: String, value: String) = response.map(_.headerMap.add(name, value))
    def getStatus = response.map(_.statusCode).getOrElse(500)
    def getStatusMessage = e.map(_.getMessage).getOrElse(null)
  }

}
