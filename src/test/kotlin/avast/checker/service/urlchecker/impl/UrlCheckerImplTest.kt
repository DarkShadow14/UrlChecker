package avast.checker.service.urlchecker.impl

import org.junit.Test
import reactor.test.StepVerifier


class UrlCheckerImplTest {
    private val urlChecker = UrlCheckerImpl()

    companion object {
        val listToTest = listOf("www.gorregle.com", "www.google.com", "https://www.gorregle.com", "http://www.google.com", "https://www.google.com", "https://google.com")
    }

    @Test
    fun checkTest() {

        val source = urlChecker.check(listToTest)

        StepVerifier.create(source)
            .expectNext(Pair("www.gorregle.com", 500),
                Pair("www.google.com", 500),
                Pair("https://www.gorregle.com", 500),
                Pair("http://www.google.com", 200),
                Pair("https://www.google.com", 200),
                Pair("https://google.com", 200))
            .verifyComplete()
    }
}