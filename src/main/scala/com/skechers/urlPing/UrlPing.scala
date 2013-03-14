package com.skechers.urlPing

import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.{HttpMethod, HttpClient}
import org.slf4j.{Logger, LoggerFactory}
import scopt.mutable.OptionParser

/**
 * Created with IntelliJ IDEA.
 * User: randy
 * Date: 3/12/13
 * Time: 12:04 PM
 */

object UrlPing {

  case class Config(url: String, timeout: Int, pause: Int, verbose: Boolean)

  def time(f: => Any): Long = {
    val b4 = System.currentTimeMillis()
    f
    val after = System.currentTimeMillis()
    after - b4
  }


  def main(args: Array[String]) {

    var conf = Config( "localhost", 100, 1000, verbose = false)

    val parser = new OptionParser {
      intOpt("t", "timelimit", "timelimit determines the max request time in milliseconds before logging a warning. Default is 100", {v: Int => conf = conf.copy(timeout = v)})
      intOpt("p", "pause", "pause is the time in milliseconds to wait between requests. Default is 1000.", {v: Int => conf = conf.copy(pause = v)})
      booleanOpt("v", "verbose", "if verbose is enabled, will log to stdout as well as urlPing.log", {v: Boolean => conf = conf.copy(verbose = v)})
      arg("<url>", "<url> is the url to ping", {v: String => conf = conf.copy(url = v)})
    }

    if(!parser.parse(args)) System.exit(-1)

    val log = LoggerFactory.getLogger(getClass)

    if (!conf.verbose) org.apache.log4j.Logger.getRootLogger.removeAppender("stdout")

    val url = conf.url

    val client = new HttpClient
    var i = 0
    while(true) {
      var get:HttpMethod = null
      try{
        i += 1
        get = new GetMethod(url)
        val duration = time { client.executeMethod(get) }
        val status: String = get.getStatusText
        if (status!= "OK") throw new IllegalStateException("Got non 200 response!")
        get.releaseConnection()

        log.info(s"URL: ${conf.url} Status: $status Time: $duration")
        if (duration > conf.timeout) log.warn(s"URL: ${conf.url} TIME > ${conf.timeout} : $duration")
      } catch {
        case t:Throwable => log.error("Error: ", t)
      } finally {
        get.releaseConnection()
      }
      Thread.sleep(conf.pause)
    }
  }
}
