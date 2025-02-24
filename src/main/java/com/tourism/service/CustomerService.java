package com.tourism.service;

import com.tourism.api.security.SecurityConfig;
import com.tourism.model.domain.Customer;
import com.tourism.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public Customer save(Customer customer) {
        customer.setPassword(SecurityConfig.passwordEncoder().encode(customer.getPassword()));
        return customerRepository.save(customer);
    }


    public Optional<Customer> getById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Customer customer = customerRepository.findCustomerByEmail(email).
                orElseThrow(() -> new RuntimeException("Customer not found with id: " + email));

        String role = customer.getRole().name();

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                customer.getEmail(),
                customer.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(role)));
        return userDetails;
    }
}