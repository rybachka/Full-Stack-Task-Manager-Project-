package com.mariia.task_manager.security;

import com.mariia.task_manager.model.Role;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

//    @Value("${JWT_SECRET_KEY}")
//    private String SECRET_KEY;

    private final String SECRET_KEY;

    public JwtUtil() {
        Dotenv dotenv = Dotenv.load();  // Load .env file
        SECRET_KEY = dotenv.get("JWT_SECRET_KEY");
        System.out.println("JWT_SECRET_KEY: " + SECRET_KEY);

    }
    private final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());//Converts the raw String key into a cryptographic key using HMAC SHA-256

    }

    public String generateToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username){
        return (username.equals(extractUsername(token)) && !isTokenExpired(token)); //Compares the extracted username with the provided username
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String extractRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }



    //Jwts.builder() to create/sign JWT Tokens, Jwts.parserBuilder() to read/verify JWT Tokens
}
