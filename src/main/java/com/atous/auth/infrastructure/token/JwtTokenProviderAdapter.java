package com.atous.auth.infrastructure.token;

import com.atous.auth.application.dto.result.*;
import com.atous.auth.application.port.out.*;
import com.atous.auth.domain.model.*;
import com.atous.auth.infrastructure.security.SecurityProperties;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {
    private final JwtEncoder encoder; private final JwtDecoder decoder; private final ClockProviderPort clock; private final SecurityProperties props;
    public JwtTokenProviderAdapter(JwtEncoder encoder, JwtDecoder decoder, ClockProviderPort clock, SecurityProperties props){this.encoder=encoder;this.decoder=decoder;this.clock=clock;this.props=props;}
    public AccessTokenResult generateAccessToken(User user){
        var now=clock.now(); var ttl=Duration.ofMinutes(props.jwt().accessTokenTtlMinutes());
        var roles=user.roles().stream().map(Role::name).collect(Collectors.toUnmodifiableSet());
        var permissions=user.roles().stream().flatMap(r->r.permissions().stream()).map(Permission::name).collect(Collectors.toUnmodifiableSet());
        var claims=JwtClaimsSet.builder().issuer(props.jwt().issuer()).subject(user.id().value().toString()).issuedAt(now).expiresAt(now.plus(ttl)).claim("email", user.email().value()).claim("roles", roles).claim("permissions", permissions).build();
        return new AccessTokenResult(encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),"Bearer",ttl.toSeconds(),now,now.plus(ttl));
    }
    public TokenClaimsDto validate(String token){
        var jwt=decoder.decode(token);
        return new TokenClaimsDto(jwt.getId(), UUID.fromString(jwt.getSubject()), jwt.getSubject(), jwt.getIssuer()==null?null:jwt.getIssuer().toString(), Set.copyOf(jwt.getAudience()), safe(jwt.getClaimAsStringList("scope")), safe(jwt.getClaimAsStringList("roles")), safe(jwt.getClaimAsStringList("permissions")), jwt.getIssuedAt(), jwt.getNotBefore(), jwt.getExpiresAt());
    }
    private Set<String> safe(List<String> v){return v==null?Set.of():Set.copyOf(v);}
}
