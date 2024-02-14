package com.dwag1n.app.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Duo Wang
 * @version: v1.0
 */
@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        List<GrantedAuthority> authorities = new ArrayList<>();
        // put groups into authorities
        groups.forEach(group -> authorities.add((GrantedAuthority) () -> "ROLE_" + group));
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
