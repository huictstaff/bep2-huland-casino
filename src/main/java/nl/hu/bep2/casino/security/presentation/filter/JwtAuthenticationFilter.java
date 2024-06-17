package nl.hu.bep2.casino.security.presentation.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import nl.hu.bep2.casino.security.domain.User;
import nl.hu.bep2.casino.security.presentation.dto.Login;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tries to authenticate a user, based on the incoming request.
 * <p>
 * Once authenticated, it will return a Bearer token (JWT) set in the
 * Authorization header of the 200 Response.
 * <p>
 * This exact Bearer has to be added in the Authorization header of subsequent
 * requests to restricted endpoints.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final SecretKey signingKey;
    private final Integer expirationInMs;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(
            String path,
            SecretKey signingKey,
            Integer expirationInMs,
            AuthenticationManager authenticationManager
    ) {
        super(new AntPathRequestMatcher(path));

        this.signingKey = signingKey;
        this.expirationInMs = expirationInMs;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        Login login = new ObjectMapper()
                .readValue(request.getInputStream(), Login.class);

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.username, login.password)
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .header().add("typ", "JWT").and()
                .issuer("huland-casino-api")
                .audience().add("huland-casino").and()
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + this.expirationInMs))
                .claim("rol", roles)
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .signWith(signingKey)
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
    }
}
