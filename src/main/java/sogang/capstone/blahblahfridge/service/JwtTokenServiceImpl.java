package sogang.capstone.blahblahfridge.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import sogang.capstone.blahblahfridge.dto.TokenDTO;

public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Override
    public String encodeJwtToken(TokenDTO tokenDTO) {
        Date now = new Date();

        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer("blah")
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis()))
            .claim("id", tokenDTO.getId())
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
            .compact();
    }


}
