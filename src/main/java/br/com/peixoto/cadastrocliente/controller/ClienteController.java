package br.com.peixoto.cadastrocliente.controller;


import br.com.peixoto.cadastrocliente.entities.Cliente;
import br.com.peixoto.cadastrocliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;


    @GetMapping
    private ResponseEntity<List<Cliente>>listarClientes(){
        return ResponseEntity.status(HttpStatus.OK).body(service.listarClientes());
    }

    @PostMapping
    private ResponseEntity<Cliente>cadastraCliente(@RequestBody Cliente cliente){
        System.out.println("Recebido" + cliente);
        Cliente clienteSalvo = service.cadastraCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deletarCliente(@PathVariable Long id){
        boolean deletado = service.deletarCliente(id);
        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
