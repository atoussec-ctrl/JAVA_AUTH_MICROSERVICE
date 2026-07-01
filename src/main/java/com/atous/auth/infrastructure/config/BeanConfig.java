package com.atous.auth.infrastructure.config;

import com.atous.auth.application.mapper.UserApplicationMapper;
import com.atous.auth.application.port.out.*;
import com.atous.auth.application.service.*;
import com.atous.auth.domain.service.*;
import com.atous.auth.infrastructure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class BeanConfig {
    @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
    @Bean PasswordPolicyService passwordPolicyService(){return new PasswordPolicyService();}
    @Bean LoginPolicyService loginPolicyService(){return new LoginPolicyService();}
    @Bean UserDomainService userDomainService(){return new UserDomainService();}
    @Bean UserApplicationMapper userApplicationMapper(){return new UserApplicationMapper();}
    @Bean RegisterUserService registerUserService(UserRepositoryPort u,PasswordHasherPort h,ClockProviderPort c,PasswordPolicyService p){return new RegisterUserService(u,h,c,p);}
    @Bean AuthenticateUserService authenticateUserService(UserRepositoryPort u, RefreshTokenRepositoryPort r, PasswordHasherPort h, TokenProviderPort t, RefreshTokenGeneratorPort g, TokenHashingPort th, ClockProviderPort c, LoginPolicyService lp, UserApplicationMapper m, SecurityProperties props){return new AuthenticateUserService(u,r,h,t,g,th,c,lp,m,Duration.ofDays(props.refreshToken().ttlDays()));}
    @Bean RefreshTokenService refreshTokenService(RefreshTokenRepositoryPort r, UserRepositoryPort u, RefreshTokenGeneratorPort g, TokenHashingPort th, TokenProviderPort t, ClockProviderPort c, UserApplicationMapper m, SecurityProperties props){return new RefreshTokenService(r,u,g,th,t,c,m,Duration.ofDays(props.refreshToken().ttlDays()));}
    @Bean LogoutService logoutService(RefreshTokenRepositoryPort r, TokenHashingPort th, ClockProviderPort c){return new LogoutService(r,th,c);}
    @Bean ValidateTokenService validateTokenService(TokenProviderPort t){return new ValidateTokenService(t);}
    @Bean GetAuthenticatedUserService getAuthenticatedUserService(UserRepositoryPort u, UserApplicationMapper m){return new GetAuthenticatedUserService(u,m);}
    @Bean ChangePasswordService changePasswordService(UserRepositoryPort u, RefreshTokenRepositoryPort r, PasswordHasherPort h, PasswordPolicyService p, ClockProviderPort c){return new ChangePasswordService(u,r,h,p,c);}
    @Bean SearchUsersService searchUsersService(UserRepositoryPort u, UserApplicationMapper m){return new SearchUsersService(u,m);}
    @Bean UpdateUserStatusService updateUserStatusService(UserRepositoryPort u, ClockProviderPort c, UserApplicationMapper m){return new UpdateUserStatusService(u,c,m);}
    @Bean AssignRolesToUserService assignRolesToUserService(UserRepositoryPort u, RoleRepositoryPort r, ClockProviderPort c, UserApplicationMapper m){return new AssignRolesToUserService(u,r,c,m);}
    @Bean CreateRoleService createRoleService(RoleRepositoryPort r, PermissionRepositoryPort p, ClockProviderPort c, UserApplicationMapper m){return new CreateRoleService(r,p,c,m);}
    @Bean UpdateRoleService updateRoleService(RoleRepositoryPort r, PermissionRepositoryPort p, ClockProviderPort c, UserApplicationMapper m){return new UpdateRoleService(r,p,c,m);}
    @Bean DeleteRoleService deleteRoleService(RoleRepositoryPort r){return new DeleteRoleService(r);}
    @Bean ForgotPasswordService forgotPasswordService(UserRepositoryPort u, PasswordResetTokenRepositoryPort pr, RefreshTokenGeneratorPort g, TokenHashingPort th, EmailSenderPort e, ClockProviderPort c){return new ForgotPasswordService(u,pr,g,th,e,c);}
    @Bean ResetPasswordService resetPasswordService(PasswordResetTokenRepositoryPort pr, UserRepositoryPort u, RefreshTokenRepositoryPort rr, PasswordHasherPort h, TokenHashingPort th, PasswordPolicyService p, ClockProviderPort c){return new ResetPasswordService(pr,u,rr,h,th,p,c);}
    @Bean EnableMfaService enableMfaService(){return new EnableMfaService();}
    @Bean VerifyMfaService verifyMfaService(ClockProviderPort c){return new VerifyMfaService(c);}
    @Bean DisableMfaService disableMfaService(ClockProviderPort c){return new DisableMfaService(c);}
}
