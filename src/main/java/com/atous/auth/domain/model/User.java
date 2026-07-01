package com.atous.auth.domain.model;

import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.PasswordHash;
import com.atous.auth.domain.valueobject.UserId;
import java.time.Instant;
import java.util.Set;

public final class User {
    private final UserId id;
    private final String name;
    private final Email email;
    private final PasswordHash passwordHash;
    private final Set<Role> roles;
    private final boolean enabled;
    private final boolean emailVerified;
    private final boolean mfaEnabled;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant lastLoginAt;

    private User(Builder b) {
        this.id=b.id; this.name=b.name; this.email=b.email; this.passwordHash=b.passwordHash;
        this.roles=Set.copyOf(b.roles); this.enabled=b.enabled; this.emailVerified=b.emailVerified; this.mfaEnabled=b.mfaEnabled;
        this.createdAt=b.createdAt; this.updatedAt=b.updatedAt; this.lastLoginAt=b.lastLoginAt;
    }

    public static Builder builder(){return new Builder();}
    public boolean canLogin(){return enabled;}
    public boolean hasRole(String roleName){return roles.stream().anyMatch(r -> r.name().equalsIgnoreCase(roleName));}
    public User withPasswordHash(PasswordHash hash, Instant at){return copy().passwordHash(hash).updatedAt(at).build();}
    public User withLastLoginAt(Instant at){return copy().lastLoginAt(at).updatedAt(at).build();}
    public User withEnabled(boolean enabled, Instant at){return copy().enabled(enabled).updatedAt(at).build();}
    public User withRoles(Set<Role> roles, Instant at){return copy().roles(roles).updatedAt(at).build();}
    private Builder copy(){return builder().id(id).name(name).email(email).passwordHash(passwordHash).roles(roles).enabled(enabled).emailVerified(emailVerified).mfaEnabled(mfaEnabled).createdAt(createdAt).updatedAt(updatedAt).lastLoginAt(lastLoginAt);}

    public UserId id(){return id;} public String name(){return name;} public Email email(){return email;} public PasswordHash passwordHash(){return passwordHash;} public Set<Role> roles(){return roles;}
    public boolean enabled(){return enabled;} public boolean emailVerified(){return emailVerified;} public boolean mfaEnabled(){return mfaEnabled;} public Instant createdAt(){return createdAt;} public Instant updatedAt(){return updatedAt;} public Instant lastLoginAt(){return lastLoginAt;}

    public static final class Builder {
        private UserId id; private String name; private Email email; private PasswordHash passwordHash; private Set<Role> roles=Set.of();
        private boolean enabled=true; private boolean emailVerified=false; private boolean mfaEnabled=false; private Instant createdAt; private Instant updatedAt; private Instant lastLoginAt;
        public Builder id(UserId v){id=v;return this;} public Builder name(String v){name=v;return this;} public Builder email(Email v){email=v;return this;} public Builder passwordHash(PasswordHash v){passwordHash=v;return this;}
        public Builder roles(Set<Role> v){roles=v==null?Set.of():Set.copyOf(v);return this;} public Builder enabled(boolean v){enabled=v;return this;} public Builder emailVerified(boolean v){emailVerified=v;return this;}
        public Builder mfaEnabled(boolean v){mfaEnabled=v;return this;} public Builder createdAt(Instant v){createdAt=v;return this;} public Builder updatedAt(Instant v){updatedAt=v;return this;} public Builder lastLoginAt(Instant v){lastLoginAt=v;return this;}
        public User build(){
            if(id==null) throw new IllegalStateException("User id is required");
            if(name==null||name.isBlank()) throw new IllegalStateException("Name is required");
            if(email==null) throw new IllegalStateException("Email is required");
            if(passwordHash==null) throw new IllegalStateException("Password hash is required");
            if(createdAt==null) throw new IllegalStateException("Created at is required");
            name=name.trim(); return new User(this);
        }
    }
}
