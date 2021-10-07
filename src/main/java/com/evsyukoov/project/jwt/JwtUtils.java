package com.evsyukoov.project.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    public static final String jwtSecret;

    public static final int TWO_HOURS = 2 * 60 * 60 * 1000;

    static {
        StringBuilder sb = new StringBuilder();
        char[] randomNumberOrAlphaOrUppercase = new char[3];
        for (int i = 0; i < 512; i++) {
            randomNumberOrAlphaOrUppercase[0] = (char) (48 + (int)(Math.random() * 9));
            randomNumberOrAlphaOrUppercase[1] = (char) (65 + (int)(Math.random() * 24));
            randomNumberOrAlphaOrUppercase[2] = (char) (97 + (int)(Math.random() * 24));
            sb.append(randomNumberOrAlphaOrUppercase[(int)(Math.random() * 3)]);
        }
        jwtSecret = sb.toString();
    }

    public static String generateJwtToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(new Date(new Date().getTime() + TWO_HOURS))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).
                parseClaimsJws(jwt).getBody().getSubject();
    }

    public static Date getExpireDateFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).
                parseClaimsJws(jwt).getBody().getExpiration();
    }
}
