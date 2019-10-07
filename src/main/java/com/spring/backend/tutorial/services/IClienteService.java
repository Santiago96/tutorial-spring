package com.spring.backend.tutorial.services;

import com.spring.backend.tutorial.model.Cliente;
import com.spring.backend.tutorial.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IClienteService {

    List<Cliente> findAll();
}
