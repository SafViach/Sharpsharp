package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.AuthAccessTokenResponseDTO;
import com.sharp.sharpshap.dto.AuthRequestDTO;
import com.sharp.sharpshap.dto.ResponseTradePointsDTO;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final TradePointService tradePointService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<ResponseTradePointsDTO> login(@RequestBody @Valid AuthRequestDTO requestDTO) {
        logger.info("AuthController: /login");

        String accessToken = authService.authenticateUserLoad(requestDTO);

        ResponseTradePointsDTO responseTradePointsDTO = tradePointService.getAllTradePoints();


        logger.info("AuthController: /login  ---настраиваем настройки для AccessToken");
        ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true) // Только через HTTPS
                .path("/")
                .maxAge(JwtService.getAccessExpirationMs()) //время жизни токена в cookie
                .domain("localhost") // или свой домен
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseTradePointsDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("AuthController: /refresh  ---Получаем аутентифицированного пользователя");
        AuthAccessTokenResponseDTO authAccessTokenResponseDTO = authService.refresh(userDetails);
        logger.info("AuthController: /refresh  ---настраиваем настройки для AccessToken");
        logger.info("Записываю токен в HttpOnly cookie:" + authAccessTokenResponseDTO.getAccessToken());
        ResponseCookie cookie = ResponseCookie.from("access_token", authAccessTokenResponseDTO.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtService.getAccessExpirationMs())
                .build();

        authAccessTokenResponseDTO.setAccessToken(null);
        logger.info("Записываю токен в HttpOnly cookie:" + authAccessTokenResponseDTO.getAccessToken());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        logger.info("AuthController: /logout  ---Получаем аутентифицированного пользователя: " + login);

        logger.info("AuthController: /logout  ---Находим пользователя по логину: " + login);
        User user = userService.getUserByLogin(login);

        logger.info("AuthController: /logout  ---Удаляем RefreshToken из БД");
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());

        logger.info("AuthController: /logout  ---Очищаем AccessToken из HttpOnly cookie");
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // удаляет куку
                .build();

        return ResponseEntity.ok().build();
    }
}
