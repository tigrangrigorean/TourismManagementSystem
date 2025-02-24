package com.tourism.api.controller;

import com.tourism.model.domain.Customer;
import com.tourism.service.CustomerService;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/public/customer")
public class PublicCustomerController {

    private final CustomerService customerService;

    @Autowired
    public PublicCustomerController(CustomerService customerService) {
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
}
