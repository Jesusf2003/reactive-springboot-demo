package com.webflux.controller;

import com.webflux.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.*;

@Slf4j
@RestController
@RequestMapping("/reactive")
public class CustomerController {

	@Autowired
	private CustomerRepository repository;

	@GetMapping
	public Flux<Customer> findCustomers() {
		log.info("Mostrando todos los datos.");
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<Customer> findCustomerById(@PathVariable Long id) {
		log.info("Mostrando dato con el id: "+ id);
		return repository.findById(id);
	}
	
	@PostMapping("/create")
	public Mono<Customer> newCustomer(@RequestBody Customer data) {
		if (data == null) {
			log.error("Problemas al recoger datos");
			return null;
		}
		log.info("Nuevo Customer registrado: "+ data.toString());
		return repository.save(data);
	}
	
	@PutMapping("/update/{id}")
	
	public Mono<Customer> modCustomer(@RequestBody Customer data, @PathVariable Long id) {
		if (data == null) {
			log.error("Problemas al recoger datos");
			return null;
		}
		log.info("Nuevo Customer actualizado: "+ data.toString());
		return repository
				.findById(id)
				.map(
					(c) -> {
						c.setName(data.getName());
						c.setEmail(data.getEmail());
						c.setPhone(data.getPhone());
						return c;
					}
				)
				.flatMap(
					c -> repository.save(c)
				);
	}
	
	@DeleteMapping("/delete/{id}")
	public Mono<Void> delCustomer(@PathVariable Long id) {
		if (id == 0 || id == null) {
			log.error("Problemas al recoger datos");
			return null;
		}
		log.info("Nuevo Customer registrado: "+ id);
		return repository.deleteById(id);
	}
}