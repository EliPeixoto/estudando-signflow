package br.com.peixoto.cadastrocliente.dto;

import java.util.List;

public record ClienteResponse(
        int paginaAtual,
        int totalPaginas,
        long totalClientes,
        List<ClienteDto> clientes
) {
}
