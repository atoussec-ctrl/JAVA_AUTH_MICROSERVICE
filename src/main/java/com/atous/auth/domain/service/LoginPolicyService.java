package com.atous.auth.domain.service;

import com.atous.auth.domain.exception.InvalidCredentialsException;
import com.atous.auth.domain.model.User;

public final class LoginPolicyService {
    public void validateCanLogin(User user) {
        if (user == null || !user.canLogin())
            throw new InvalidCredentialsException();
    }
}
