package br.com.peixoto.cadastrocliente.service;

import br.com.peixoto.cadastrocliente.dto.ClienteDto;
import br.com.peixoto.cadastrocliente.dto.ClienteResponse;
import br.com.peixoto.cadastrocliente.entities.Cliente;
import br.com.peixoto.cadastrocliente.exceptions.EmailJaCadastradoException;
import br.com.peixoto.cadastrocliente.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;


    public ClienteResponse listarClientes(Pageable pageable) {
        Page<Cliente> listaClientes = repository.findAll(pageable);

        List<ClienteDto> clienteDto = listaClientes.getContent()
                .stream()
                .map(cliente -> new ClienteDto(cliente.getId(), cliente.getNome(), cliente.getEmail()))
                .collect(Collectors.toList());

        return new ClienteResponse(
                listaClientes.getNumber(),
                listaClientes.getTotalPages(),
                listaClientes.getTotalElements(),
                clienteDto
        );
}

    public Cliente buscarClientePorId(Long id) {
        Cliente cliente = repository.findById(id).orElse(null);
        return cliente;

    }

    public Cliente atualizaCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));
        if (clienteExistente == null) {
            throw new RuntimeException("Cliente nao encontrado com id: " + id);
        }
        String emailCliente = clienteAtualizado.getEmail();
        if (clienteExistente.getEmail().equals(clienteAtualizado.getEmail())) {
            throw new EmailJaCadastradoException("Email " + clienteAtualizado.getEmail() + " já cadastrado");
        }
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());

        return repository.save(clienteExistente);
    }

    public Cliente buscarClientePorEmail(String email) {
        Cliente clienteExistente = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));

        return clienteExistente;
    }

    public List<Cliente> buscarPorNomeParcial(String nome) {
        List<Cliente> clienteExistentes = repository.findByNomeContainingIgnoreCase(nome);
        if (clienteExistentes == null) {
            throw new RuntimeException("Cliente nao encontrado com email: " + nome);
        }
        return clienteExistentes;
    }

    public Cliente cadastraCliente(Cliente cliente) {
        String email = cliente.getEmail();
        Optional<Cliente> clienteExistente = repository.findByEmail(email);
        if (clienteExistente.isPresent()) {
            throw new EmailJaCadastradoException("Email " + cliente.getEmail() + " ja cadastrado");
        }
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
