package br.com.peixoto.cadastrocliente.controller;

import br.com.peixoto.cadastrocliente.service.SignNowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/signnow")
public class SignNowController {
    @Value("${api.signnow.basic_auth_token}")
    private String basicAuthToken;

    @Value("${api.signnow.username}")
    private String username;

    @Value("${api.signnow.password}")
    private String password;

    @Value("${api.signnow.url}")
    private String baseUrl;

    private final SignNowService signNowService;

    public SignNowController(SignNowService signNowService, WebClient.Builder webClientBuilder) {
        this.signNowService = signNowService;
    }

    @GetMapping("/token")
    public Mono<ResponseEntity<String>> getToken() {
        return signNowService.obterToken(basicAuthToken, username, password)
                .map(token -> ResponseEntity.ok("Token obtido: " + token))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Erro ao obter token: " + e.getMessage())));
    }

    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> uploadArquivo(@RequestParam("file") MultipartFile arquivo) {

        return signNowService.obterToken(basicAuthToken, username, password)
                .flatMap(token -> {
                    try {
                        // Criar um arquivo temporário para armazenar o upload
                        Path arquivoTemporario = Files.createTempFile("upload_", "_" + arquivo.getOriginalFilename());
                        Files.copy(arquivo.getInputStream(), arquivoTemporario, StandardCopyOption.REPLACE_EXISTING);

                        // Enviar o arquivo para a SignNow usando o token obtido
                        return signNowService.uploadArquivo(token, arquivoTemporario.toString())
                                .map(response -> ResponseEntity.ok("Arquivo enviado com sucesso: " + response));
                    } catch (Exception e) {
                        return Mono.just(ResponseEntity.badRequest().body("Erro ao salvar arquivo: " + e.getMessage()));
                    }
                });
    }

}
