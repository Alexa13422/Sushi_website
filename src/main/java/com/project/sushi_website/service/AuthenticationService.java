package com.project.sushi_website.service;

import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.CustomerDTO;
import com.project.sushi_website.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer signup(CustomerDTO input) {
        Customer customer = new Customer();
        customer.setUsername(input.getUsername());
               customer.setEmail(input.getEmail());
                customer.setPassword(passwordEncoder.encode(input.getPassword()));

        return customerRepository.save(customer);
    }

    public Customer authenticate(CustomerDTO input) throws Exception {
        // Fetch the customer from the repository
        Customer customer = customerRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new Exception("User does not exist"));

        // Check if the password matches using BCryptPasswordEncoder
        if (!passwordEncoder.matches(input.getPassword(), customer.getPassword())) {
            throw new Exception("Incorrect password");
        }

        return customer;
    }
}
