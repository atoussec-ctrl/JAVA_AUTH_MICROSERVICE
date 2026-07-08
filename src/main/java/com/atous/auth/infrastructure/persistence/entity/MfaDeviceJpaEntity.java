package com.atous.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="mfa_devices")
public class MfaDeviceJpaEntity {
    @Id private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(name="secret_hash", nullable=false) private String secretHash;
    @Column(nullable=false) private boolean enabled;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="verified_at") private Instant verifiedAt;
    protected MfaDeviceJpaEntity(){}
    public MfaDeviceJpaEntity(UUID id, UUID userId, String secretHash, boolean enabled, Instant createdAt, Instant verifiedAt){
        this.id=id; this.userId=userId; this.secretHash=secretHash; this.enabled=enabled; this.createdAt=createdAt; this.verifiedAt=verifiedAt;
    }
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public UUID getUserId(){return userId;} public void setUserId(UUID userId){this.userId=userId;}
    public String getSecretHash(){return secretHash;} public void setSecretHash(String secretHash){this.secretHash=secretHash;}
    public boolean isEnabled(){return enabled;} public void setEnabled(boolean enabled){this.enabled=enabled;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant createdAt){this.createdAt=createdAt;}
    public Instant getVerifiedAt(){return verifiedAt;} public void setVerifiedAt(Instant verifiedAt){this.verifiedAt=verifiedAt;}
}
