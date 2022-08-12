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
import reactor.core.publisher.Mono
import java.util.function.Function
import javax.servlet.http.HttpServletResponse

class WebClientFilter (
    private val config: CMTConfigSyncServiceConfigurationProperties
        ) : Function<ClientRequest, Mono<ClientRequest>> {

    private var parsedToken = ""

    private val logger: Logger = LoggerFactory.getLogger(CMTConfigSyncService::class.java)

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
    }

    override fun apply(t: ClientRequest): Mono<ClientRequest> {

        if(parsedToken.isEmpty()) {
            parsedToken = getToken()
        }
        return Mono.just(
            ClientRequest
                .from(t)
                .header("Authorization", "Bearer $parsedToken")
                .build())
    }
}