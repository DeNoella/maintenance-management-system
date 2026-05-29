package com.example.demo.Repository;

import com.example.demo.Enum.Role;
import com.example.demo.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByUsername(String username);
    List<User> findByBranchId(Long branchId);
    List<User> findByRole(Role role);
    List<User> findByBranchIdAndRole(Long branchId, Role role);
    List<User> findByIsActiveTrue();
    boolean existsByUsername(String username);
}
