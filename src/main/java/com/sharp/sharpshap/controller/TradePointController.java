package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ResponseTradePointDTO;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.error.ErrorResponse;
import com.sharp.sharpshap.exceptions.TradePointNotFoundException;
import com.sharp.sharpshap.service.JwtService;
import com.sharp.sharpshap.service.TradePointService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tradePoints")
public class TradePointController {
    private final TradePointService tradePointService;
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping
    public ResponseEntity<Object> getAllTradePoints() {
        try {
            return ResponseEntity.ok().body(tradePointService.getAllTradePoints());
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException("TradePointController: ---getAllTradePoints Торговые точки не найдены"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{uuidTradePoint}")
    public ResponseEntity<ResponseTradePointDTO> getTradePoint(HttpServletRequest request,
                                                    @PathVariable("uuidTradePoint") UUID uuidTradePoint) {
        logger.info("tradePoints: /select-trade-point  ---настраиваем настройки для uuidTradePoint");
        ResponseCookie cookie = ResponseCookie.from("uuidTradePoint", uuidTradePoint.toString())
                .httpOnly(true)
                .secure(true) // Только через HTTPS
                .path("/")
                .maxAge(JwtService.getAccessExpirationMs()) //время жизни токена в cookie
                .domain("localhost") // или свой домен
                .build();
        UUID uuidUser = (UUID) request.getAttribute("uuidUser");
        ResponseTradePointDTO responseTradePointDTO= tradePointService.setTradePointForUser(uuidUser, uuidTradePoint);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseTradePointDTO);

    }

    @PostMapping
    public ResponseEntity<Object> createTradePoint(@Valid @RequestBody TradePoint tradePoint,
                                                   BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getField())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body("Объект не валиден" + errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tradePointService.createTradePoint(tradePoint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTradePoint(@PathVariable UUID id,
                                                   @Valid @RequestBody TradePoint tradePoint,
                                                   BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errors = result.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining(","));
                return ResponseEntity.badRequest().body("Объект не валиден" + errors);
            }
            return ResponseEntity.ok().body(tradePointService.updateTradePoint(id, tradePoint));
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException(""), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTradePoint(@PathVariable UUID uuid) {
        try {
            tradePointService.deleteByIdTradePoint(uuid);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException(""), HttpStatus.NOT_FOUND);
        }
    }

}
