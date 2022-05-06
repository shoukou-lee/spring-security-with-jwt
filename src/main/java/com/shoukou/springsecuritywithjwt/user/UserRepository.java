package com.shoukou.springsecuritywithjwt.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name = :username")
    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("select u from User u where u.phoneNumber = :email")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.role = 'ROLE_USER'")
    List<User> findAllUsers();
}
