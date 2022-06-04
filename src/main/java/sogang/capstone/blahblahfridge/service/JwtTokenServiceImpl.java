package sogang.capstone.blahblahfridge.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sogang.capstone.blahblahfridge.dto.TokenDTO;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:/application.properties")
public class JwtTokenServiceImpl implements JwtTokenService {

    private final CustomUserDetailServiceImpl customUserDetailsService;
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Override
    public String encodeJwtToken(TokenDTO tokenDTO) {
        Date now = new Date();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer("blah")
            .setIssuedAt(now)
            .setSubject(tokenDTO.getId().toString())
            .setExpiration(new Date(now.getTime() + Duration.ofMinutes(10080).toMillis()))
            .claim("id", tokenDTO.getId())
            .claim("roles", "USER")
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
            .compact();
    }

    @Override
    public String getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(JWT_SECRET)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(
            this.getUserIdFromJwtToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }


    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
}
