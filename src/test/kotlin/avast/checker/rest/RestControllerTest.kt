package avast.checker.rest

import avast.checker.service.urlchecker.UrlChecker
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toFlux

@RunWith(SpringRunner::class)
@WebFluxTest(controllers = [RestController::class])
class RestControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var urlChecker: UrlChecker

    companion object {
        val listToTest = listOf("www.gorregle.com", "www.google.com", "https://www.gorregle.com", "http://www.google.com", "https://www.google.com", "https://google.com")
        val fluxResponse = listOf(Pair("www.gorregle.com", 500),
                                    Pair("www.google.com", 500),
                                    Pair("https://www.gorregle.com", 500),
                                    Pair("http://www.google.com", 200),
                                    Pair("https://www.google.com", 200),
                                    Pair("https://google.com", 200)).toFlux()

        val expectedJson = "[\n" +
                "    {\n" +
                "        \"first\": \"www.gorregle.com\",\n" +
                "        \"second\": 500\n" +
                "    },\n" +
                "    {\n" +
                "        \"first\": \"www.google.com\",\n" +
                "        \"second\": 500\n" +
                "    },\n" +
                "    {\n" +
                "        \"first\": \"https://www.gorregle.com\",\n" +
                "        \"second\": 500\n" +
                "    },\n" +
                "    {\n" +
                "        \"first\": \"http://www.google.com\",\n" +
                "        \"second\": 200\n" +
                "    },\n" +
                "    {\n" +
                "        \"first\": \"https://www.google.com\",\n" +
                "        \"second\": 200\n" +
                "    },\n" +
                "    {\n" +
                "        \"first\": \"https://google.com\",\n" +
                "        \"second\": 200\n" +
                "    }\n" +
                "]"
    }

    @Test
    fun checkUrlsTest() {
        `when`(urlChecker.check(listToTest)).thenReturn(fluxResponse)
        webTestClient.post()
            .uri("/checkUrls")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("[\"www.gorregle.com\", \"www.google.com\", \"https://www.gorregle.com\", \"http://www.google.com\", \"https://www.google.com\", \"https://google.com\"]")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .json(expectedJson)
    }
}