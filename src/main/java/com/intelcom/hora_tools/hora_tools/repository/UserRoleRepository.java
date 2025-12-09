package com.intelcom.hora_tools.hora_tools.repository;

import com.intelcom.hora_tools.hora_tools.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    
    List<UserRole> findByUserId(String userId);
    
    void deleteByUserId(String userId);
    
    boolean existsByUserIdAndRole(String userId, String role);
}
