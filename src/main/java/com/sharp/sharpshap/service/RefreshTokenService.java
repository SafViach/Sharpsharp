package com.sharp.sharpshap.service;

import com.sharp.sharpshap.entity.RefreshToken;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.RefreshTokenRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Transactional
    public String generateRefreshToken(UUID userUuid) {
        logger.info("RefreshTokenService: ---generateRefreshToken");
        User user = userRepository.findById(userUuid).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RefreshToken refreshTokenEntity = new RefreshToken();

        String refreshToken = jwtService.generateRefreshToken(user.getId());
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusHours(12));
        refreshTokenEntity.setUser(user);
        logger.info("RefreshTokenService: ---generateRefreshToken сохраняем RefreshToken в БД");
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    public boolean isRefreshTokenValid(String token) {
        logger.info("RefreshTokenService: ---isRefreshTokenValid");
        RefreshToken rf = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new RuntimeException("RefreshTokenService: isRefreshTokenValid ---RefreshToken в БД не найден"));
        UUID userUuid = jwtService.getUuidFromRefreshToken(token);
        return jwtService.isRefreshTokenValid(token, userUuid);
    }

    public UUID getUserIdFromRefreshToken(String token) {
        logger.info("RefreshTokenService: ---getUserIdFromRefreshToken ---Получение uuidUser из RefreshToken");
        if (isRefreshTokenValid(token)) {
            return jwtService.getUuidFromRefreshToken(token);
        }
        logger.error("RefreshTokenService: ---getUserIdFromRefreshToken ---Не получен uuidUser из RefreshToken");
        return null;
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        logger.info("RefreshTokenService: ---deleteRefreshToken");
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public void deleteRefreshTokenByUserId(UUID uuidUser) {
        logger.info("RefreshTokenService: ---deleteRefreshTokenByUserId");
        refreshTokenRepository.deleteByUserId(uuidUser);
    }
}
