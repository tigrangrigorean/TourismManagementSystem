package com.tourism.api.controller;

import com.tourism.model.domain.Customer;
import com.tourism.service.CustomerService;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/customers")
public class PrivateCustomerController {

    private final CustomerService customerService;

    @Autowired
    public PrivateCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)));
    }

    @GetMapping("/")
    private ResponseEntity<List<Customer>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PostMapping("/save")
    private ResponseEntity<Customer> save(@RequestBody Customer customer) {
        return ResponseEntity.status(201).body(customerService.save(customer));
    }

    @PostMapping("/update")
    private ResponseEntity<Customer> update(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.update(customer));
    }

    @DeleteMapping("/remove")
    private ResponseEntity<String> remove(@RequestParam Long id) {
        customerService.delete(id);
        return ResponseEntity.ok().body("Customer with id " + id + " removed successfully");
    }
}