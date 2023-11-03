package com.example.clothesshop.dao.repository;

import com.example.clothesshop.dao.entity.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerDetail, Long> {

}
