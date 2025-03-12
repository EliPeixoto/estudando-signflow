package br.com.peixoto.cadastrocliente.service;

import br.com.peixoto.cadastrocliente.entities.Cliente;
import br.com.peixoto.cadastrocliente.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;


    public List<Cliente> listarClientes() {
        List<Cliente> listaClientes = repository.findAll();

        return listaClientes;
    }

    public Cliente cadastraCliente(Cliente cliente){
        return repository.save(cliente);
    }


    public boolean deletarCliente(Long id) {
        Optional<Cliente> clienteOptional = repository.findById(id);

        if (clienteOptional.isEmpty()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
