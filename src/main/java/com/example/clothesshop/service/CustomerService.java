package com.example.clothesshop.service;

import com.example.clothesshop.dao.entity.CustomerDetail;
import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.CustomerRepository;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.UserRole;
import com.example.clothesshop.reponse.CustomerDetailResponse;
import com.example.clothesshop.reponse.UserResponse;
import com.example.clothesshop.request.CustomerDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addCustomerDetail(CustomerDetailRequest customerDetailRequest) {
        CustomerDetail customerDetail = modelMapper.map(customerDetailRequest, CustomerDetail.class);
        CustomerDetail saved = customerRepository.save(customerDetail);
        CustomerDetailResponse mapped = modelMapper.map(saved, CustomerDetailResponse.class);
        mapped.setUserResponse(modelMapper.map(userRepository.findById(saved.getUserId()).get(), UserResponse.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapped);
    }

    public ResponseEntity<?> getCustomerDetailById(Long userId, Long customerId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
                CustomerDetail customerDetail = customerRepository.findById(customerId).get();
                CustomerDetailResponse mapped = modelMapper.map(customerDetail, CustomerDetailResponse.class);
                mapped.setUserResponse(modelMapper.map(userRepository.findById(customerDetail.getUserId()).get(), UserResponse.class));
                return ResponseEntity.ok(mapped);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
    }

    public ResponseEntity<?> getAllCustomersDetails(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
                List<CustomerDetailResponse> customerResponseList = new ArrayList<>();
                List<CustomerDetail> customerDetailList = customerRepository.findAll();
                for (CustomerDetail customerDetail : customerDetailList) {
                    CustomerDetailResponse mapped = modelMapper.map(customerDetail, CustomerDetailResponse.class);
                    mapped.setUserResponse(modelMapper.map(userRepository.findById(customerDetail.getUserId()), UserResponse.class));
                    customerResponseList.add(mapped);
                }
                return ResponseEntity.ok(customerResponseList);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public ResponseEntity<?> updateCustomerDetail(CustomerDetailRequest customerDetailRequest) {
        Optional<CustomerDetail> optionalCustomer = customerRepository.findById(customerDetailRequest.getId());
        if (optionalCustomer.isPresent()) {
            CustomerDetail customerDetail = optionalCustomer.get();
            customerDetail.setHeight(customerDetailRequest.getHeight());
            customerDetail.setWeight(customerDetailRequest.getWeight());
            customerDetail.setShoulderMeasurement(customerDetailRequest.getShoulderMeasurement());
            customerDetail.setWaistMeasurement(customerDetailRequest.getWaistMeasurement());
            customerDetail.setSideMeasurement(customerDetailRequest.getSideMeasurement());
            customerDetail.setUserId(customerDetail.getUserId());
            CustomerDetail saved = customerRepository.save(customerDetail);
            CustomerDetailResponse mapped = modelMapper.map(saved, CustomerDetailResponse.class);
            mapped.setUserResponse(modelMapper.map(userRepository.findById(saved.getUserId()).get(), UserResponse.class));
            return ResponseEntity.ok(mapped);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
    }
}
