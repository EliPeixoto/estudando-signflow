package br.com.peixoto.cadastrocliente.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class SignNowService {

    private final WebClient webClient;

    public SignNowService(WebClient.Builder webClientBuilder,
                          @Value("${api.signnow.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Mono<String> obterToken(String basicAuthToken,
                                   String username,
                                   String password,
                                   String scope,
                                   String expirationTime) {

        return webClient.post()
                .uri("") // A URI já está no baseUrl
                .header("Accept", "application/json")
                .header("Authorization", "Basic " + basicAuthToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters
                        .fromFormData("username", username)
                        .with("password", password)
                        .with("grant_type", "password")
                        .with("scope", scope)
                        .with("expiration_time", expirationTime)) // Omitir se não for obrigatório
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.err.println("Erro ao obter token: " + error.getMessage()));
    }
}