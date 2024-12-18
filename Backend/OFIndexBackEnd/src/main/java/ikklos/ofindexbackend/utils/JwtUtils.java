package ikklos.ofindexbackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

//https://blog.csdn.net/shenyunmomie/article/details/139805325
public class JwtUtils {

    public static final String Claims_UserID="USER_ID";

    public static String createJwt(String secretKey, long ttlMillis, Map<String, Object> claims) {
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        JwtBuilder builder = Jwts.builder()
                .signWith(key)
                .claims(claims)
                .expiration(exp);
        return builder.compact();
    }

    public static Claims parseJWT(String secretKey, String token) {

        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 得到DefaultJwtParser
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        return jws.getPayload();
    }

    public static String generateSecretKey(){
        SecureRandom secureRandom=new SecureRandom();
        var keyBuilder=Jwts.SIG.HS256.key().random(secureRandom);
        return keyBuilder.build().toString();
    }

}
