package com.intelcom.hora_tools.hora_tools.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "`user_group`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserGroup.UserGroupId.class)
public class UserGroup {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "group_id", nullable = false)
    private String groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Composite Primary Key class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGroupId implements Serializable {
        private String userId;
        private String groupId;
    }
}
