package com.tourism.service;

import com.tourism.api.security.SecurityConfig;
import com.tourism.exceptions.CustomExceptions;
import com.tourism.model.domain.Customer;
import com.tourism.model.dto.ForgotPassword;
import com.tourism.model.dto.Verify;
import com.tourism.model.vo.Role;
import com.tourism.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final MailSenderService mailSenderService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager,@Lazy MailSenderService mailSenderService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mailSenderService = mailSenderService;
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

    public String hashPassword(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }


    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    public Customer signUp(Customer customer) {
        customer.setRole(Role.USER);
        customer.setPassword(hashPassword(customer.getPassword()));
        customer.setVerifyMail(false);
        return customerRepository.save(customer);
    }

    /**
     * @param verify
     * @return
     */
    public String verify(Verify verify) {
        Optional<Customer> customer = findCustomerByEmail(verify.getEmail());
        if (customer.get().getVerifyMail()) {
            throw new CustomExceptions.AlreadyVerifiedEmail("Your email is already verified.");
        }
        if (!customer.get().getPin().equals(verify.getPin())) {
            throw new CustomExceptions.InvalidPinExceptions("Invalid PIN.");
        }
        Customer existingCustomer = customer.get();
        existingCustomer.setVerifyMail(true);
        update(existingCustomer);

        return "Mail Verified successfully";
    }

    public String changePassword(String email, String oldPassword, String newPassword, String newPasswordRepeat) {

        Optional<Customer> optionalCustomer = customerRepository.findCustomerByEmail(email);
        Customer customer = optionalCustomer.get();

        if (!customer.getVerifyMail()) {
            throw new CustomExceptions.NotVerifiedMailException("Mail must be verified");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oldPassword));
        } catch (Exception e) {
            throw new CustomExceptions.InvalidOldPasswordException("Old password is incorrect.");
        }

        if (passwordEncoder.matches(newPassword, customer.getPassword())) {
            throw new CustomExceptions.InvalidNewPasswordException("New password cannot be the same as the current password.");
        }

        if (!newPassword.equals(newPasswordRepeat)) {
            throw new CustomExceptions.PasswordMismatchException("New passwords do not match.");
        }

        customer.setPassword(hashPassword(newPassword));
        customerRepository.save(customer);

        return "Password updated successfully.";
    }

    public String forgotPassword(ForgotPassword forgotPassword) {
        Optional<Customer>optionalCustomer = findCustomerByEmail(forgotPassword.getEmail());
        Customer existingCustomer = optionalCustomer.get();

        if (!existingCustomer.getVerifyMail()) {
            throw new CustomExceptions.NotVerifiedMailException("Mail must be verified");
        }
        String newPassword = forgotPassword.generatePassword();
        existingCustomer.setPassword(hashPassword(newPassword));
        update(existingCustomer);
        mailSenderService.sendEmail(forgotPassword.getEmail(),"New password",newPassword);
        return "New password has been send to email";
    }
}