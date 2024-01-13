package kr.project.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.project.backend.auth.ServiceUser;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    public static ServiceUser decode(String token, String jwtSecretKey) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token)
                .getBody();

        ObjectMapper mapper = new ObjectMapper();

        ServiceUser serviceUser = null;

        try {
            String jsonStr = mapper.writeValueAsString(claims);
            serviceUser = mapper.readValue(jsonStr,ServiceUser.class);
        }catch (Exception e){
            throw new Exception();
        }
        return serviceUser;
    }
    public static boolean isExpired(String token, String jwtSecretKey){
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
    }
    public static String createJwt(UUID userId, String userEmail, String secretKey, Long expiredHs){
        Claims claIms = Jwts.claims();
        claIms.put("userId",userId);
        claIms.put("userEmail",userEmail);

        return Jwts.builder()
                .setClaims(claIms)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredHs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
