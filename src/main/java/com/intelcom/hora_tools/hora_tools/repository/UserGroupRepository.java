package com.intelcom.hora_tools.hora_tools.repository;

import com.intelcom.hora_tools.hora_tools.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroup.UserGroupId> {
    
    List<UserGroup> findByUserId(String userId);
    
    List<UserGroup> findByGroupId(String groupId);
    
    void deleteByUserId(String userId);
    
    void deleteByGroupId(String groupId);
    
    boolean existsByUserIdAndGroupId(String userId, String groupId);
}
