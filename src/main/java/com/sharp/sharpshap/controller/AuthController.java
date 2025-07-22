package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.AuthAccessTokenResponseDTO;
import com.sharp.sharpshap.dto.AuthRequestDTO;
import com.sharp.sharpshap.dto.AuthResponseDTO;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.UserRepository;
import com.sharp.sharpshap.service.AuthService;
import com.sharp.sharpshap.service.JwtService;
import com.sharp.sharpshap.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO requestDTO, HttpServletResponse response) {
        logger.info("AuthController: /login");

        AuthResponseDTO responseDTO = authService.authenticateUserLoad(requestDTO);

        logger.info("AuthController: /login  ---настраиваем настройки для AccessToken");
        ResponseCookie cookie = ResponseCookie.from("access_token", responseDTO.getAccessToken())
                .httpOnly(true)
                .secure(true) // Только через HTTPS
                .path("/")
                .maxAge(JwtService.getAccessExpirationMs()) //время жизни токена в cookie
                .domain("localhost") // или свой домен
                .build();

        responseDTO.setAccessToken(null);
        //"Вопрос: нельзя передавать Польз RefreshToken если тут бужет null - это плохо?"
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessTokenResponseDTO> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
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
        //"Вопрос: нельзя передавать Польз RefreshToken если тут бужет null - это плохо?"
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authAccessTokenResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        logger.info("AuthController: /logout  ---Получаем аутентифицированного пользователя: " + login);

        logger.info("AuthController: /logout  ---Находим пользователя по логину: " + login);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("AuthController: /refresh  ---Пользователь не найден"));

        logger.info("AuthController: /logout  ---Удаляем RefreshToken из БД");
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());

        logger.info("AuthController: /logout  ---Очищаем AccessToken из HttpOnly cookie");
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // удаляет куку
                .build();

        return ResponseEntity.ok().body("Смена закрыта");
    }
}
