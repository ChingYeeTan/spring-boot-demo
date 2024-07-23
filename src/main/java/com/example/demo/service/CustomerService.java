package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Supplier;

@Service
public class CustomerService {

    protected static final Supplier<EntityNotFoundException> RECORD_NOT_FOUND_SUPPLIER = () -> new EntityNotFoundException("Unable to find entity");

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public Customer saveCustomer(Customer Customer) {
        return customerRepository.save(Customer);
    }

    @Transactional
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(RECORD_NOT_FOUND_SUPPLIER);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id).orElseThrow(RECORD_NOT_FOUND_SUPPLIER);

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        // Update other fields as necessary

        return customerRepository.save(customer);
    }

}
