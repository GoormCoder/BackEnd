package goormcoder.webide.jwt;

import goormcoder.webide.domain.Member;
import goormcoder.webide.domain.enumeration.MemberRole;
import goormcoder.webide.security.MemberDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L * 7; // 7일

    private final SecretKey secretKey;

    // 수정전
    // public JwtProvider(@Value("${jwt.secret}") String secretKey) {
    //     String encodeKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    //     this.secretKey = Keys.hmacShaKeyFor(encodeKey.getBytes());
    // }

    // 수정후
    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String getUsername(String token) {
        return getClaims(token).get("loginId", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String issueAccessToken(final Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String issueRefreshToken(final Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public JwtValidation validateToken(String token) {
        try {
            getClaims(token);
            return JwtValidation.JWT_VALID;
        } catch (Exception e) {
            return JwtValidation.JWT_INVALID;
        }
    }

    public Authentication getAuthenticationFromToken(String token) {
        String loginId = getUsername(token);
        String role = getRole(token);

        MemberDetails memberDetails = new MemberDetails(Member.builder()
                .loginId(loginId)
                .password("password")
                .role(MemberRole.valueOf(role.toUpperCase()))
                .build());

        return new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    }

    private String generateToken(Authentication authentication, long expirationTime) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime));

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        claims.put("loginId", memberDetails.getUsername());
        claims.put("role", memberDetails.getAuthorities().stream().toList().get(0).getAuthority());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
