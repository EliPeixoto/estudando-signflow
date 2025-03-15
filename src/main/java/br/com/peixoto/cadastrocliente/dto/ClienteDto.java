package br.com.peixoto.cadastrocliente.dto;

public record ClienteDto(
        Long id,
        String nome,
        String email
) {
}
