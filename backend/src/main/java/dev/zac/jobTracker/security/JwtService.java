package dev.zac.jobTracker.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.zac.jobTracker.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service for handling JWT token operations.
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Extract username from JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract a specific claim from JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver function to extract the claim
     * @param <T> the type of the claim
     * @return the claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate JWT token for user.
     *
     * @param userDetails the user details
     * @return the JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }    /**
     * Generate JWT token with extra claims.
     *
     * @param extraClaims additional claims to include
     * @param userDetails the user details
     * @return the JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtProperties.expiration());
    }

    /**
     * Generate refresh token.
     *
     * @param userDetails the user details
     * @return the refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtProperties.expiration() * 7); // 7 times longer
    }

    /**
     * Build JWT token.
     *
     * @param extraClaims additional claims
     * @param userDetails user details
     * @param expiration token expiration time
     * @return the JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Check if token is valid for the given user.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if token is valid
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Check if token is expired.
     *
     * @param token the JWT token
     * @return true if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract all claims from token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }    /**
     * Get the signing key for JWT.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
