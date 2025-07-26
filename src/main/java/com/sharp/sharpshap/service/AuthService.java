package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.AuthAccessTokenResponseDTO;
import com.sharp.sharpshap.dto.AuthRequestDTO;
import com.sharp.sharpshap.dto.AuthResponseDTO;
import com.sharp.sharpshap.entity.RefreshToken;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.exceptions.RefreshTokenNotFound;
import com.sharp.sharpshap.repository.RefreshTokenRepository;
import com.sharp.sharpshap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class AuthService {
    public final RefreshTokenRepository refreshTokenRepository;
    public final RefreshTokenService refreshTokenService;
    public final JwtService jwtService;
    public final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TradePointService tradePointService;
    public static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String authenticateUserLoad(AuthRequestDTO requestDTO) {
        logger.info("AuthService: authenticateUserLoad requestDTO.getPassword()-> " + requestDTO.getPassword());
        logger.info("AuthService: authenticateUserLoad requestDTO.getLogin()-> " + requestDTO.getLogin());

        String login = requestDTO.getLogin();

        logger.info("AuthService: authenticateUserLoad ---поиск пользователя по логину");
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        logger.info("AuthService: authenticateUserLoad ---Authentication");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getLogin(), requestDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        logger.info("AuthService: authenticateUserLoad ---Генерируем и сохраняем в БД RefreshToken");
        refreshTokenService.generateRefreshToken(user.getId());

        logger.info("AuthService: authenticateUserLoad Authentication генерируем AccessToken");
        return jwtService.generateAccessToken(user.getId());

    }

    public AuthAccessTokenResponseDTO refresh(UserDetails userDetails){
        logger.info("AuthService:  refresh ---Получаем текущего аутентифицированного пользователя");
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("" +
                "AuthService:  refresh ---Пользователь с логином " + login + " не найден"));

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RefreshTokenNotFound("AuthService:  refresh ---Токен не найден"));

        logger.info("AuthService:  refresh ---Проверяем на валидность RefreshToken");
        boolean isValid = jwtService.isRefreshTokenValid(refreshTokenEntity.getToken(), user.getId());
        if (!isValid){
            throw new RuntimeException("AuthController:  refresh ---RefreshТокен не валиден");

        }
        logger.info("AuthService:  refresh ---результат:" + isValid);

        logger.info("AuthService:  refresh ---генерируем новый Access Token");
        String newAccessToken = jwtService.generateAccessToken(user.getId());
        logger.info("AuthService:  refresh ---Проверка нового токена на ASCII:" + isASCII(newAccessToken));

        AuthAccessTokenResponseDTO authAccessTokenResponseDTO
                = new AuthAccessTokenResponseDTO( newAccessToken);
        return authAccessTokenResponseDTO;
    }

    private boolean isASCII (String accessToken){
        if (!accessToken.chars().allMatch(c -> c < 128)){
            throw new IllegalArgumentException("AccessToken содержит не ASCII симводы");
        }
        return true;
    }
}
