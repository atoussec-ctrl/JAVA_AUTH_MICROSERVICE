package com.atous.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="mfa_recovery_codes")
public class MfaRecoveryCodeJpaEntity {
    @Id private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(name="code_hash", nullable=false, unique=true, length=255) private String codeHash;
    @Column(nullable=false) private boolean used;
    @Column(name="created_at", nullable=false) private Instant createdAt;
    @Column(name="used_at") private Instant usedAt;
    protected MfaRecoveryCodeJpaEntity(){}
    public MfaRecoveryCodeJpaEntity(UUID id, UUID userId, String codeHash, boolean used, Instant createdAt, Instant usedAt){
        this.id=id; this.userId=userId; this.codeHash=codeHash; this.used=used; this.createdAt=createdAt; this.usedAt=usedAt;
    }
    public UUID getId(){return id;}
    public UUID getUserId(){return userId;}
    public String getCodeHash(){return codeHash;}
    public boolean isUsed(){return used;} public void setUsed(boolean used){this.used=used;}
    public Instant getCreatedAt(){return createdAt;}
    public Instant getUsedAt(){return usedAt;} public void setUsedAt(Instant usedAt){this.usedAt=usedAt;}
}
