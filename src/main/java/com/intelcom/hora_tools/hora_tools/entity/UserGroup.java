package com.intelcom.hora_tools.hora_tools.entity;

import java.io.Serializable;

import com.intelcom.hora_tools.hora_tools.entity.UserGroup.UserGroupId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`user_group`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserGroupId.class)
@EqualsAndHashCode(exclude = { "user" })
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
