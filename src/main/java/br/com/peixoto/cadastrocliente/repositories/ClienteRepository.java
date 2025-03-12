package br.com.peixoto.cadastrocliente.repositories;

import br.com.peixoto.cadastrocliente.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository  extends JpaRepository<Cliente, Long> {
}
