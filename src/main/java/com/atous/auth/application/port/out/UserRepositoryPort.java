package com.atous.auth.application.port.out;

import com.atous.auth.application.dto.view.PageView;
import com.atous.auth.domain.model.User;
import com.atous.auth.domain.valueobject.Email;
import com.atous.auth.domain.valueobject.UserId;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(UserId userId);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    User save(User user);
    PageView<User> search(String search, Boolean enabled, int page, int size);
}
