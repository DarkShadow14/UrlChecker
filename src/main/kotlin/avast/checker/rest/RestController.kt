package avast.checker.rest

import avast.checker.service.urlchecker.UrlChecker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class RestController {

    @Autowired
    private lateinit var urlChecker: UrlChecker

    @PostMapping("/checkUrls")
    @ResponseStatus(HttpStatus.OK)
    fun checkUrls(@RequestBody urls: List<String>): Flux<Pair<String, Int>> {
        println("Checking urls: $urls with flux")
        return urlChecker.check(urls)
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun welcome(): String {
        return "Welcome to URL Checker"
    }
}