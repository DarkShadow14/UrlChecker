package avast.checker.service.urlchecker

import reactor.core.publisher.Flux

interface UrlChecker {
    fun check(urls: List<String>?): Flux<Pair<String, Int>>
}