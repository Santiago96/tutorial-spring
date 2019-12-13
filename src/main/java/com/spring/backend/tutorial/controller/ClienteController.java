package com.spring.backend.tutorial.controller;


import com.spring.backend.tutorial.model.Cliente;
import com.spring.backend.tutorial.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {

    private static final String MAIN_ROUTE = "/api";
    private static final String BASIC_ENDPOINT = "/clientes";
    private static final String BASIC_ENDPOINT_PATH = "/clientes/{id}";

    @Autowired
    IClienteService clienteService;

    @GetMapping(BASIC_ENDPOINT)
    public List<Cliente> getClientes(){
        return clienteService.findAll();
    }

    @GetMapping(BASIC_ENDPOINT_PATH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getClienteById(@PathVariable Long id){
        Cliente cliente = null;
        Map<String,Object> response = new HashMap<>();

        try{
            cliente = clienteService.findById(id);
        }catch(DataAccessException e){
            response.put("mensaje","Error realizando consulta a la BD");
            response.put("error",e.getMessage()+" "+e.getMostSpecificCause());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(null == cliente){
            response.put("mensaje","El cliente ID: "+ id + " no existe en la base de datos");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
    }

    @PostMapping(BASIC_ENDPOINT)
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente){
        Cliente newcliente = null;
        Map<String,Object> response = new HashMap<>();
        try{
            newcliente = clienteService.save(cliente);
        }catch(DataAccessException e){
            response.put("mensaje","Error al realizar el insert");
            response.put("error",e.getMessage()+" "+e.getMostSpecificCause());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El cliente ha sido creado con éxito");
        response.put("cliente", cliente);

        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    @PutMapping(BASIC_ENDPOINT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente, @PathVariable Long id){
        Cliente currentCliente = clienteService.findById(id);
        Cliente clienteUpdated;
        Map<String,Object> response = new HashMap<>();

        if(null == currentCliente){
            response.put("mensaje","Error: no se pudo editar, el cliente con ID: "+id+" no existe");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        currentCliente.setNombre(cliente.getNombre());
        currentCliente.setApellido(cliente.getApellido());
        currentCliente.setEmail(cliente.getEmail());

        try {
            clienteUpdated = clienteService.save(currentCliente);
        }catch(DataAccessException e){
                response.put("mensaje","Error al actualizar en la base de datos");
                response.put("error",e.getMessage()+" "+e.getMostSpecificCause());
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Cliente actualizado");
        response.put("cliente", clienteUpdated);

        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
    }

    @DeleteMapping(BASIC_ENDPOINT_PATH)
    public ResponseEntity<?> delete(@PathVariable Long id){
        Map<String,Object> response = new HashMap<>();
        try{
            clienteService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje","Error al eliminar cliente de la base de datos");
            response.put("error",e.getMessage()+": "+e.getMostSpecificCause());
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","Cliente eliminado con éxito");
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
    }
}
