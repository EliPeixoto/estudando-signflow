package br.com.peixoto.cadastrocliente.controller;

import br.com.peixoto.cadastrocliente.service.SignNowService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class SignNowController {

    @Value("${api.signnow.basic_auth_token}")
    private String authToken;

    @Value("${api.signnow.username}")
    private String user;

    @Value("${api.signnow.password}")
    private String password;



    private final SignNowService signNowService;

    public SignNowController(SignNowService signNowService) {
        this.signNowService = signNowService;
    }

    @GetMapping("/token")
    public Mono<String> getToken() {
        return signNowService.obterToken(
                authToken,
                user,
                password,
                "*", // Scope (coloque conforme necessidade)
                "60" // Expiração (60 segundos por padrão)
        );
    }
}
