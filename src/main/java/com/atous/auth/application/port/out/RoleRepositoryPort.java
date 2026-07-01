package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.Role;
import com.atous.auth.domain.valueobject.RoleId;
import java.util.*;

public interface RoleRepositoryPort { Optional<Role> findById(RoleId id); Optional<Role> findByName(String name); List<Role> findAllByIds(Collection<RoleId> ids); List<Role> findAll(); Role save(Role role); void deleteById(RoleId id); }
