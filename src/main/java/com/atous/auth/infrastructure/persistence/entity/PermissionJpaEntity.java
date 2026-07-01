package com.atous.auth.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="permissions")
public class PermissionJpaEntity {
    @Id private UUID id;
    @Column(nullable=false, unique=true, length=120) private String name;
    @Column(length=255) private String description;
    protected PermissionJpaEntity(){}
    public PermissionJpaEntity(UUID id, String name, String description){this.id=id; this.name=name; this.description=description;}
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
}
