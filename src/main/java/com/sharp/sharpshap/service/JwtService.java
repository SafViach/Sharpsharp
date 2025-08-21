package com.sharp.sharpshap.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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

    private SecretKey getAccessSigningKey() {
        logger.info("JwtService: ---getAccessSigningKey Получение секретного ключа AccessToken");
        return Keys.hmacShaKeyFor(JWT_SECRET_ACCESS_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshSigningKey() {
        logger.info("JwtService: ---getRefreshSigningKey Получение секретного ключа RefreshToken");
        return Keys.hmacShaKeyFor(JWT_SECRET_REFRESH_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        logger.info("JwtService: ---isTokenExpired Проверка токена на истечения срока");
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // токен уже просрочен
        } catch (JwtException e) {
            logger.warn("Ошибка при проверке срока действия токена: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String generateAccessToken(UUID userId) {
        logger.info("JwtService: ---generateAccessToken Генерируем AccessToken");
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_MS))//15 min
                .signWith(getAccessSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        logger.info("JwtService: ---generateRefreshToken Генерируем RefreshToken");
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_MS))//7 days
                .signWith(getRefreshSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public UUID getUuidFromAccessToken(String token) {
        logger.info("JwtService: ---getUuidFromAccessToken Получение uuidUser из AccessToken");

        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getAccessSigningKey())
                    .build()
                    .parseSignedClaims(token);

            String uuid = jws.getPayload().getSubject();
            return UUID.fromString(uuid);

        } catch (JwtException e) {
            logger.warn("Ошибка при проверке срока действия токена: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public UUID getUuidWithoutExpirationDateCheckFromAccessToken(String token) throws MalformedJwtException
            , IllegalArgumentException {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getAccessSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return UUID.fromString(jws.getPayload().getSubject());

        } catch (ExpiredJwtException e) {
            logger.warn("Токен просрочен, но подпись верна — извлекаем UUID");
            return UUID.fromString(e.getClaims().getSubject());
        } catch (MalformedJwtException | SignatureException e) {
            logger.error("Подпись невалидна", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Некорректный аргумент", e);
            throw e;
        }
    }

    public UUID getUuidFromRefreshToken(String token) {
        logger.info("JwtService: ---getUuidFromRefreshToken Получение uuidUser из RefreshToken");
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getRefreshSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return UUID.fromString(jws.getPayload().getSubject());
        } catch (ExpiredJwtException e) {
            logger.warn("Refresh token expired", e);
            throw new IllegalArgumentException("Refresh token expired", e);
        } catch (JwtException e) {
            logger.warn("Invalid refresh token: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }

    public boolean isAccessTokenValid(String token, UUID userId) throws IllegalArgumentException, JwtException {
        logger.info("JwtService: ---isAccessTokenValid Проверка валидании AccessToken");
        try {
            UUID actualId = getUuidFromAccessToken(token);
            return actualId.equals(userId) && !isTokenExpired(token, getAccessSigningKey());
        } catch (Exception e) {
            return false;
        }

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
