package ikklos.ofindexbackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretKeyBuilder;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//https://blog.csdn.net/shenyunmomie/article/details/139805325
public class JwtUtils {

    public static final String Claims_UserID="USER_ID";

    public static final long KeyDuration = 60*1000;

    private static String secretKey0;

    static {
        JwtKeyFactory factory=new JwtKeyFactory();
        secretKey0 = factory.generateJwtKey();
    }

    public static class JwtKeyFactory {

        private final SecretKeyBuilder keyBuilder;

        public JwtKeyFactory(){
            SecureRandom sr = new SecureRandom();
            keyBuilder=Jwts.SIG.HS256.key().random(sr);
        }

        public String generateJwtKey(){
            return Base64.getEncoder().encodeToString(keyBuilder.build().getEncoded());
        }

    }

    public static String createJwt(SecretKey secretKey,long ttlMillis, Map<String, Object> claims) {
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .signWith(secretKey)
                .claims(claims)
                .expiration(exp);
        return builder.compact();
    }

    public static String createJwt(Map<String, Object> claims){
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey0));
        return createJwt(key,KeyDuration,claims);
    }

    public static Claims parseJWT(String secretKey, String token){

        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        // 得到DefaultJwtParser
        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        return jws.getPayload();
    }

    public static Claims parseJWT(String token){
        return parseJWT(secretKey0,token);
    }

    public static Integer getUserIdJWT(String token){

        if(token.charAt(0)=='?'){
            return Integer.valueOf(token.substring(1));
        }

        var claims=JwtUtils.parseJWT(token);
        return (Integer) claims.get(JwtUtils.Claims_UserID);
    }

    public static String createUserIdJWT(Integer userId){

        Map<String, Object> claims=new HashMap<>();
        claims.put(JwtUtils.Claims_UserID,userId);

        return JwtUtils.createJwt(claims);
    }

    public static void refreshJwtKey(){
        var factory=new JwtKeyFactory();
        secretKey0 =factory.generateJwtKey();
    }

}
