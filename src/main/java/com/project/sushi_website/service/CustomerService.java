package com.project.sushi_website.service;

import com.project.sushi_website.config.UserAlreadyExistException;
import com.project.sushi_website.config.UserNotExistException;
import com.project.sushi_website.model.Customer;
import com.project.sushi_website.model.DTO.CustomerDTO;
import com.project.sushi_website.repository.CustomerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CustomerService{

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customers;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    public void registerNewCustomerAccount(CustomerDTO customerDTO) throws UserAlreadyExistException {
        Customer customer = getCustomerByEmail(customerDTO.getEmail());
        customer.setUsername(customerDTO.getUsername());
        customer.setPassword(customerDTO.getPassword());
        customer.setEmail(customerDTO.getEmail());
        customer.setAdmin(false);
        customerRepository.save(customer);
    }
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow();
    }

    public void updateCustomer(CustomerDTO customerDTO) throws Exception {
        Optional<Customer> customer = customerRepository.findByEmail(customerDTO.getEmail());
        if (customer.isPresent()) {
            Customer customerGet = customer.get();
            customerGet.setUsername(customerDTO.getUsername());
            customerGet.setEmail(customerDTO.getEmail());
            customerGet.setPassword(customerDTO.getPassword());
            customerGet.setPhone(customerDTO.getPhone());
            customerGet.setCity(customerDTO.getCity());
            customerGet.setAddress(customerDTO.getAddress());
            customerRepository.save(customerGet);
        } else throw new Exception("user not exist");
    }


}

