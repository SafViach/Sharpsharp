package com.sharp.sharpshap.filter;

import com.sharp.sharpshap.config.MyUserDetailsService;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.UserRepository;
import com.sharp.sharpshap.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    public static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("JwtFilter: ---doFilterInternal ---Запуск JWTFilter");

        String path = request.getServletPath();
        logger.info("JwtFilter: ---request.getServletPath()->" + path);


        logger.info("JwtFilter: ---if (path.startsWith(\"/h2-console\") || path.startsWith(\"/api/auth/login\"))->"
                + path.startsWith("/h2-console") + " " + path.startsWith("/api/auth/login"));
        // Пропускаем определённые пути без проверки токена
        if (path.startsWith("/h2-console") || path.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("JwtFilter: ---Извлекаем AccessToken из HttpOnly cookie");
        String accessToken = extractTokenFromCookies(request);
        if (accessToken != null) {

            logger.info("JwtFilter: ---Извлекаем UUID пользователя из AccessToken");
            UUID uuidUser = jwtService.getUuidFromAccessToken(accessToken);

            logger.info("JwtFilter: ---Провереям токен на валидность");
            boolean result = jwtService.isAccessTokenValid(accessToken, uuidUser);

            logger.info("JwtFilter: ---Result: " + result);
            if (result) {
                logger.info("JwtFilter: ---Поиск пользователя по uuid");
                User user = userRepository.findById(uuidUser).orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь не найден"));

                logger.info("JwtFilter: ---Загрущаем пользователя .loadUserByUsername(user.getLogin());");
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getLogin());

                logger.info("JwtFilter: ---Создаём Authentication");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                logger.info("JwtFilter: ---Устанавливаем в SecurityContext");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.error("JwtFilter: access_token отсутствует в куках");
        }

        // Продолжаем цепочку фильтров
        //изменить request (добавть в header uuid пользователя)
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
