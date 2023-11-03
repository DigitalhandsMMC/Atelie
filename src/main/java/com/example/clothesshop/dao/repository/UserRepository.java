package com.example.clothesshop.dao.repository;

import com.example.clothesshop.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
