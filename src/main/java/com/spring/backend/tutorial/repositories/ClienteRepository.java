package com.spring.backend.tutorial.repositories;

import com.spring.backend.tutorial.model.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {


}
