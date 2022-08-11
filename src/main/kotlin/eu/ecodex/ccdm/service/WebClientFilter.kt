package eu.ecodex.ccdm.service

import elemental.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.trace.http.HttpTrace.Response
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import javax.servlet.http.HttpServletResponse

class WebClientFilter (
    private val config: CMTConfigSyncServiceConfigurationProperties
        ) {

    private var parsedToken = ""

    private val logger: Logger = LoggerFactory.getLogger(CMTConfigSyncService::class.java)

    var filterFunction = ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
        if(parsedToken.isEmpty()) {
            parsedToken = getToken()
        }
        next.exchange(request)
    }

    private val keycloakClient = WebClient.builder()
        .baseUrl(config.keycloakUrl)
        .build()

    private fun getToken(): String {

        val block = keycloakClient.post()
            .uri(config.keycloakUrl)
            .body(
                BodyInserters.fromFormData("grant_type", "password")
                .with("username", config.username)
                .with("password", config.pw)
                .with("client_id", config.clientId)
            )
            .retrieve()
            .bodyToMono(String::class.java)

        val parsedJson = Json.parse(block.block())
        val parsedToken = parsedJson.getString("access_token")

        logger.trace("Retrieved authentication token: [{}]", parsedToken)

        return parsedToken

        // Json parsing: https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
    }

    // 3 conditions:
    // 1) Check if token empty -> if yes, get new token
    // 2) Check if token expired -> if no, get new token
    // 3) Check if server request = 401 -> get new token --> filter response

    // https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/webflux-client/webflux-client-filter.html

    // https://www.baeldung.com/spring-webclient-filters

}