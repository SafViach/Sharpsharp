package com.sharp.sharpshap.filter;

import com.sharp.sharpshap.config.MyUserDetailsService;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.UserRepository;
import com.sharp.sharpshap.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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


        logger.info("JwtFilter: ---if (path.startsWith(h2-console) || path.startsWith(/api/auth/login))->"
                + path.startsWith("/h2-console") + " " + path.startsWith("/api/auth/login"));

        // Пропускаем определённые пути без проверки токена
        if (path.startsWith("/h2-console") || path.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("JwtFilter: ---Извлекаем AccessToken из HttpOnly cookie");
        try {
            String accessToken = extractTokenFromCookies(request);
            if (accessToken != null) {
                UUID uuidUser = null;

                logger.info("JwtFilter: ---!path.startsWith(api/auth/refresh)" + !path.startsWith("/api/auth/refresh"));
                if (!path.startsWith("/api/auth/refresh")) {
                    logger.info("JwtFilter: ---Провереям токен на валидность :"
                            + jwtService.isAccessTokenValid(accessToken, uuidUser));
                    logger.info("JwtFilter: ---Извлекаем UUID пользователя из AccessToken(с проверкой срока годности)");
                    uuidUser = jwtService.getUuidFromAccessToken(accessToken);
                } else {
                    logger.info("JwtFilter: ---AccessToken не проверяем на валидность");
                    logger.info("JwtFilter: ---Извлекаем UUID пользователя из AccessToken(без проверки на сроки годности)");
                    uuidUser = jwtService.getUuidWithoutExpirationDateCheckFromAccessToken(accessToken);
                }
                logger.info("JwtFilter: ---Поиск пользователя по uuid");
                User user = userRepository.findById(uuidUser).orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь не найден"));

                logger.info("JwtFilter: ---Добавляем uuid пользователя в request");
                request.setAttribute("uuidUser", uuidUser);

                for (Cookie cookie : request.getCookies()){
                    if ("uuidTradePoint".equals(cookie.getName())){
                        UUID uuidTradePoint = UUID.fromString(cookie.getValue());
                        request.setAttribute("uuidTradePoint" , uuidTradePoint);
                    }
                }

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

            } else {
                logger.error("JwtFilter: ---AccessToken отсутствует в куках");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("---error: AccessToken отсутствует в куках");
            }
        } catch (ExpiredJwtException e) {
            logger.error("JwtFilter: ---JWT token expired", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("error: JWT token expired");
            return; // Прерываем цепочку фильтров
        } catch (MalformedJwtException | SignatureException e) {
            logger.error("JwtFilter: ---Неверный JWT токен", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("JwtFilter: ---error: Неверный JWT токен");
            return;
        } catch (UnsupportedJwtException e) {
            logger.error("JwtFilter: ---AccessToken не поддерживается");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("JwtFilter: ---error: AccessToken не поддерживается");
        } catch (IllegalArgumentException e) {
            logger.error("JwtFilter: ---AccessToken передан null или другой недопустимый аргумент");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("---error: AccessToken передан null или другой недопустимый аргумент");
        } catch (JwtException e) {
            logger.error("JwtFilter: ---error: AccessToken ошибка работы с JWT");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("JwtFilter: ---error: AccessToken ошибка работы с JWT");
        } catch (Exception e) {
            logger.error("Ошибка при обработке JWT", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("JwtFilter: ---error: Ошибка сервера");
            return;
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
