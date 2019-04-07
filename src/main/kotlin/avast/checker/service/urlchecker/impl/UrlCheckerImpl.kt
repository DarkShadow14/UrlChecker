package avast.checker.service.urlchecker.impl

import avast.checker.service.urlchecker.UrlChecker
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.RequestMethod
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

@Service
class UrlCheckerImpl: UrlChecker {

    override fun check(urls: List<String>?): Flux<Pair<String, Int>> {
        val watch = StopWatch()
        watch.start()

        return urls?.map { url ->
            Pair(url, checkUrl(url))
        }.also {
            watch.stop()
            println("" + watch.totalTimeMillis + " ms")
        }?.toFlux() ?: Flux.empty()
    }

    private fun checkUrl(url: String): Int {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = RequestMethod.HEAD.toString()
            connection.connect()
            connection.responseCode
        } catch (e: Exception) {
            println(e.localizedMessage)
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
    }
}