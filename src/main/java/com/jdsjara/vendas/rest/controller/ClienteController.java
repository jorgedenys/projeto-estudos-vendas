package com.jdsjara.vendas.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jdsjara.vendas.domain.entity.Cliente;
import com.jdsjara.vendas.domain.repository.ClienteRepository;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private ClienteRepository clienteRepository;

	public ClienteController(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@GetMapping("{id}")
	public Cliente getClienteById(@PathVariable Integer id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente save(@RequestBody @Valid Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) {
		clienteRepository.findById(id)
						 .map(clienteExistente -> {
							 clienteRepository.delete(clienteExistente);	 
							 return clienteExistente;
						 })
						 .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.") );
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Integer id,
			           @RequestBody @Valid Cliente cliente) {
		clienteRepository.findById(id)
						 .map(clienteExistente -> {
							 cliente.setId(clienteExistente.getId());
							 clienteRepository.save(cliente);
							 return cliente;
						 })
						 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
	}

	@GetMapping
	public List<Cliente> find(Cliente filtro) {

		// Critérios de pesquisa
		ExampleMatcher matcher = ExampleMatcher.matching()
											   .withIgnoreCase()
											   .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

		// Criando a estratégia do EXAMPLE
		Example example = Example.of(filtro, matcher);

		// Realiza a pesquisa baseado na estratégia Example
		return clienteRepository.findAll(example);
 
	}

}
