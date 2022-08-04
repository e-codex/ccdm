package eu.ecodex.ccdm.service

import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction

class WebClientFilter {

    val parsedToken = ""

    var filterFunction = ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
        if(parsedToken.isEmpty()) {

        }
        next.exchange(request)
    }

    // 3 conditions:
    // 1) Check if token empty -> if yes, get new token
    // 2) Check if token expired -> if no, get new token
    // 3) Check if server request = 401 -> get new token

    // https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/webflux-client/webflux-client-filter.html

    // https://www.baeldung.com/spring-webclient-filters

    /*private fun filterFunction(): ExchangeFilterFunction { (request: ClientRequest, next: ExchangeFunction) ->
        if(parsedToken.isEmpty()) {
            webClientTryout()
        }
        next.exchange(request)
    }*/

    /*var filterFunction = ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
        if(parsedToken.isEmpty()) {
            webClientTryout()
        }
        next.exchange(request)
    }*/

}