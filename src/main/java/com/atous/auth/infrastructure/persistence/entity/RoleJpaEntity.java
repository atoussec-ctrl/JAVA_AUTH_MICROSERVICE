package com.atous.auth.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class RoleJpaEntity {
    @Id private UUID id;
    @Column(nullable=false, unique=true, length=80) private String name;
    @Column(length=255) private String description;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="role_permissions", joinColumns=@JoinColumn(name="role_id"), inverseJoinColumns=@JoinColumn(name="permission_id"))
    private Set<PermissionJpaEntity> permissions = new HashSet<>();
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="updated_at") private Instant updatedAt;
    protected RoleJpaEntity(){}
    public RoleJpaEntity(UUID id, String name, String description, Set<PermissionJpaEntity> permissions, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public Set<PermissionJpaEntity> getPermissions(){return permissions;} public void setPermissions(Set<PermissionJpaEntity> permissions){this.permissions=permissions;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant updatedAt){this.updatedAt=updatedAt;}
}
