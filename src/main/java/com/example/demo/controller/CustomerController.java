package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.model.Todo;
import com.example.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/customer")
public class CustomerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "/get-all-customers")
    public ResponseEntity<Page<Customer>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("***********get all customers***********");
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerService.getAllCustomers(pageable);
        logger.info("Response: {}", customerPage.getContent());
        return ResponseEntity.ok(customerPage);
    }

    @PostMapping(value = "/create-customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        logger.info("****** Create Customer ****");
        logger.info("Request: {}", customer);
        Customer savedCustomer = customerService.saveCustomer(customer);
        logger.info("Response: {}", savedCustomer);
        return ResponseEntity.ok(savedCustomer);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Customer> editCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        logger.info("****** Edit Customer ****");
        logger.info("Request: {}", customerDetails);
        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
        logger.info("Response: {}", updatedCustomer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        logger.info("****** Get Customer By ID ****");
        logger.info("ID: {}", id);
        Customer customer = customerService.getCustomerById(id);
        logger.info("Response: {}", customer);
        return ResponseEntity.ok(customer);
    }

    @GetMapping(path = "/call-external-api")
    public ResponseEntity<Todo> callExternalApi() {
        logger.info("****** Call External API ****");
        RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter())
                .build();
        String apiUrl = "https://jsonplaceholder.typicode.com/todos/1";
        Todo response = restTemplate.getForObject(apiUrl, Todo.class);
        logger.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

}
