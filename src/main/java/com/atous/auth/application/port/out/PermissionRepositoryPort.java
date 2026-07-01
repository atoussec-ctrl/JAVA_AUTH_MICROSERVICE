package com.atous.auth.application.port.out;

import com.atous.auth.domain.model.Permission;
import java.util.*;

public interface PermissionRepositoryPort { List<Permission> findAllByNames(Collection<String> names); }
