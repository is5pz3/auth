package com.pz.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class Jwt {

    private static final long EXPIRATION_TIME = 600000;
    private static final String SECRET_KEY = "secret.key";
    private static final String ISSUER = "pz5";

    @Autowired
    private Environment env;

    public Jwt(Environment env) {
        this.env = env;
    }

    public String createJWT(String subject) {
        String secretKey = env.getProperty(SECRET_KEY);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey);


        long expMillis = nowMillis + EXPIRATION_TIME;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public Claims decodeJWT(String jwt) {
        String secretKey = env.getProperty(SECRET_KEY);
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public Date getJWTExpireDate(String jwt) {
        return decodeJWT(jwt).getExpiration();
    }
}