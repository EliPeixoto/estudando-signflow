package br.com.peixoto.cadastrocliente.controller;


import br.com.peixoto.cadastrocliente.dto.ClienteDto;
import br.com.peixoto.cadastrocliente.dto.ClienteResponse;
import br.com.peixoto.cadastrocliente.entities.Cliente;
import br.com.peixoto.cadastrocliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;


    @GetMapping
    private ResponseEntity<ClienteResponse> listarClientes(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarClientes(pageable));
    }

    @PostMapping
    private ResponseEntity<Cliente> cadastraCliente(@RequestBody @Valid Cliente cliente) {
        Cliente clienteSalvo = service.cadastraCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = service.buscarClientePorId(id);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Cliente> atualizaCliente(@PathVariable Long id, @RequestBody Cliente clienteExistente) {
        Cliente clienteAtualizado = service.atualizaCliente(id, clienteExistente);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizado);
    }

    @GetMapping("/email")
    private ResponseEntity<Cliente> buscaClientePorEmail(@RequestParam String email){
        Cliente cliente = service.buscarClientePorEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping("/nome-parcial")
    private ResponseEntity<List<Cliente>> buscarPorNomeParcial(@RequestParam String nome){
        List<Cliente> cliente = service.buscarPorNomeParcial(nome);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        boolean deletado = service.deletarCliente(id);
        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
