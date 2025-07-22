package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.error.ErrorResponse;
import com.sharp.sharpshap.exceptions.TradePointNotFoundException;
import com.sharp.sharpshap.service.TradePointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tradePoints")
public class TradePointController {
    private TradePointService tradePointService;
    @Autowired
    public void setTradePointService(TradePointService tradePointService) {
        this.tradePointService = tradePointService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllTradePoints() {
        try {
            return ResponseEntity.ok().body(tradePointService.getAllTradePoints());
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException(""),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTradePoint(@PathVariable int id) {
        return ResponseEntity.ok().body(tradePointService.getByIdTradePoint(id));

    }

    @PostMapping
    public ResponseEntity<Object> createTradePoint(@Valid @RequestBody TradePoint tradePoint,
                                                       BindingResult result) {
        if (result.hasErrors()){
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getField())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body("Объект не валиден" + errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tradePointService.createTradePoint(tradePoint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTradePoint(@PathVariable int id,
                                                   @Valid @RequestBody TradePoint tradePoint,
                                                   BindingResult result){
        try {
            if (result.hasErrors()){
                String errors = result.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining(","));
                return ResponseEntity.badRequest().body("Объект не валиден" + errors);
            }
            return ResponseEntity.ok().body(tradePointService.updateTradePoint(id , tradePoint));
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException(""),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTradePoint(@PathVariable Integer id){
        try {
            tradePointService.deleteByIdTradePoint(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ErrorResponse.error(new TradePointNotFoundException(""),HttpStatus.NOT_FOUND);
        }
    }

}
