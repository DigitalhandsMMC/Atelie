package com.example.clothesshop.controller;

import com.example.clothesshop.request.CustomerDetailRequest;
import com.example.clothesshop.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customers")
public class CustomerController {


    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<?> addCustomerDetail(@RequestBody CustomerDetailRequest customerDetailRequest) {
        return customerService.addCustomerDetail(customerDetailRequest);
    }

    @GetMapping("{userId}/getCustomer/{customerId}")
    public ResponseEntity<?> getCustomerDetailById(@PathVariable(name = "userId") Long userId, @PathVariable(name = "customerId") Long customerId) {
        return customerService.getCustomerDetailById(userId, customerId);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<?> getAllCustomersDetails(@PathVariable Long userId) {
        return customerService.getAllCustomersDetails(userId);
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomerDetail(@RequestBody CustomerDetailRequest customerDetailRequest) {
        return customerService.updateCustomerDetail(customerDetailRequest);
    }
}
