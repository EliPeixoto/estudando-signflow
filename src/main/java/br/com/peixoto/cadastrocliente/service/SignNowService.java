package br.com.peixoto.cadastrocliente.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SignNowService {


    private final WebClient webClient;

    public SignNowService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.signnow.com").build();
    }


    public Mono<String> obterToken(String basicAuthToken, String username, String password) {
        return webClient.post()
                .uri("/oauth2/token")
                .header("Authorization", "Basic " + basicAuthToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("username=" + username + "&password=" + password + "&grant_type=password")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> System.err.println("Erro ao obter token: " + error.getMessage()));
    }


    public Mono<String> uploadArquivo(String accessToken, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return Mono.error(new RuntimeException("Arquivo não encontrado: " + filePath));
            }

            return webClient.post()
                    .uri("/document")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(new FileSystemResource(file))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(error -> System.err.println("Erro ao enviar arquivo: " + error.getMessage()));

        } catch (Exception e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage()));
        }
    }
}