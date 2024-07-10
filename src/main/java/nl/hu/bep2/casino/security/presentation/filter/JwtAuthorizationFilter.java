package nl.hu.bep2.casino.security.presentation.filter;


import io.jsonwebtoken.*;
import nl.hu.bep2.casino.security.domain.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * Tries to authorize a user, based on the Bearer token (JWT) from
 * the Authorization header of the incoming request.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final SecretKey signingKey;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(
            SecretKey secret,
            AuthenticationManager authenticationManager
    ) {
        super(authenticationManager);

        this.signingKey = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = this.getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            logger.debug("Authorization header is null or empty");
            return null;
        }

        if (!token.startsWith("Bearer ")) {
            logger.debug("Authorization header does not start with Bearer");
            return null;
        }

        try{
            JwtParser jwtParser = Jwts.parser()
                    .verifyWith(this.signingKey)
                    .build();

            Jws<Claims> parsedToken = jwtParser.parseSignedClaims(token.replace("Bearer ", ""));

            var username = parsedToken.getPayload()
                    .getSubject();

            var authorities = ((List<?>) parsedToken.getPayload()
                    .get("rol")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .toList();

            if (username.isEmpty()) {
                return null;
            }

            UserProfile principal = new UserProfile(
                    username,
                    (String) parsedToken.getPayload().get("firstName"),
                    (String) parsedToken.getPayload().get("lastName")
            );

            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        }catch (MalformedJwtException | SignatureException ex){
            logger.debug(ex.getMessage());
            return null;
        }

    }
}
