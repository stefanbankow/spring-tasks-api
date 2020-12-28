package com.bankov.springtaskapi.components;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bankov.springtaskapi.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final CustomUserDetailsService userDetailsService;
    private final String secretKey = "secret";
    private final long validityInMs = 3600000;
    private final Date currentDate = new Date();
    private final Date expiryDate = new Date(currentDate.getTime() + validityInMs);
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username, List<String> roles) {
        return JWT.create()
                .withIssuer("task-manager-api")
                .withIssuedAt(currentDate)
                .withExpiresAt(expiryDate).withClaim("username", username)
                .withClaim("roles", roles).sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return JWT.decode(token).getClaim("username").asString();
    }
    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        Date expiresAt = JWT.decode(token).getExpiresAt();
        Date currentDate = new Date();

        return !expiresAt.before(currentDate);
    }
}
