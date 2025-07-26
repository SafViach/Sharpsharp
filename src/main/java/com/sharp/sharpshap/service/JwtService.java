package com.sharp.sharpshap.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Getter
public class JwtService {
    private static final long ACCESS_EXPIRATION_MS = 15 * 60 * 1000; //15 м
    private static final long REFRESH_EXPIRATION_MS = 720 * 60 * 1000; //12 ч
    private final String JWT_SECRET_ACCESS_KEY = System.getenv("JWT_SECRET_ACCESS_KEY");
    private final String JWT_SECRET_REFRESH_KEY = System.getenv("JWT_SECRET_REFRESH_KEY");
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private Key getAccessSigningKey() {
        logger.info("JwtService: ---getAccessSigningKey Получение секретного ключа AccessToken");
        return Keys.hmacShaKeyFor(JWT_SECRET_ACCESS_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private Key getRefreshSigningKey() {
        logger.info("JwtService: ---getRefreshSigningKey Получение секретного ключа RefreshToken");
        return Keys.hmacShaKeyFor(JWT_SECRET_REFRESH_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isTokenExpired(String token, Key key) {
        logger.info("JwtService: ---isTokenExpired Проверка токена на истечения срока");
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(UUID userId) {
        logger.info("JwtService: ---generateAccessToken Генерируем AccessToken");
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_MS))//15 min
                .signWith(getAccessSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        logger.info("JwtService: ---generateRefreshToken Генерируем RefreshToken");
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_MS))//7 days
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID getUuidFromAccessToken(String token) {
        logger.info("JwtService: ---getUuidFromAccessToken Получение uuidUser из AccessToken");
        String uuid = Jwts.parserBuilder()
                .setSigningKey(getAccessSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return UUID.fromString(uuid);
    }

    public UUID getUuidWithoutExpirationDateCheckFromAccessToken(String token) throws MalformedJwtException
            , IllegalArgumentException {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getAccessSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return UUID.fromString(claimsJws.getBody().getSubject());

        } catch (ExpiredJwtException e) {
            // ⭐ Ключевой момент: перехватываем expired, но достаём subject
            logger.warn("Токен просрочен, но подпись верна — извлекаем UUID");
            return UUID.fromString(e.getClaims().getSubject());
        } catch (MalformedJwtException | SignatureException e) {
            logger.error("Подпись невалидна", e);
            throw e; // пробрасываем дальше
        } catch (IllegalArgumentException e) {
            logger.error("Некорректный аргумент", e);
            throw e;
        }
    }

    public UUID getUuidFromRefreshToken(String token) {
        logger.info("JwtService: ---getUuidFromRefreshToken Получение uuidUser из RefreshToken");
        String uuid = Jwts.parserBuilder()
                .setSigningKey(getRefreshSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return UUID.fromString(uuid);
    }

    public boolean isAccessTokenValid(String token, UUID userId) throws IllegalArgumentException, JwtException {
        logger.info("JwtService: ---isAccessTokenValid Проверка валидании AccessToken");
        UUID actualId = getUuidFromAccessToken(token);
        return actualId.equals(userId) && !isTokenExpired(token, getAccessSigningKey());

    }

    public boolean isRefreshTokenValid(String token, UUID userId) {
        logger.info("JwtService: ---isRefreshTokenValid Проверка валидании RefreshToken");
        try {
            UUID actualId = getUuidFromRefreshToken(token);
            return actualId.equals(userId) && !isTokenExpired(token, getRefreshSigningKey());
        } catch (ExpiredJwtException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (ExpiredJwtException e) ---RefreshToken просрочен");
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (UnsupportedJwtException e) ---RefreshToken не поддерживается");
            return false;
        } catch (MalformedJwtException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (MalformedJwtException e) ---RefreshToken содержит неверный формат");
            return false;
        } catch (SignatureException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (SignatureException e) ---RefreshToken подпись не верна");
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (IllegalArgumentException e) ---RefreshToken передан null " +
                    "или другой недопустимый аргумент");
            return false;
        } catch (JwtException e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (JwtException e) ---RefreshToken ошибка работы с JWT");
            return false;
        } catch (Exception e) {
            logger.error("JwtService: ---isRefreshTokenValid catch (Exception e) ---RefreshToken ошибка валидации");
            return false;
        }
    }

    public static long getAccessExpirationMs() {
        return ACCESS_EXPIRATION_MS;
    }

    public static long getRefreshExpirationMs() {
        return REFRESH_EXPIRATION_MS;
    }
}
