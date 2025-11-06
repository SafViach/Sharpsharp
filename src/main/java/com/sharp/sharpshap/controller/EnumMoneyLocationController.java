package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ResponseEnumMoneyLocationDTO;
import com.sharp.sharpshap.service.EnumMoneyLocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/moneyLocation")
public class EnumMoneyLocationController {
    private final EnumMoneyLocationService moneyLocationService;
    private static final Logger logger = LoggerFactory.getLogger(EnumMoneyLocationController.class);

    @GetMapping
    public ResponseEntity<List<ResponseEnumMoneyLocationDTO>> getAllEnumMoneyLocation() {
        logger.info("EnumMoneyLocationController: ---getAllEnumMoneyLocation");
        return ResponseEntity.ok().body(moneyLocationService.getAllMoneyLocationDTO());
    }

    @GetMapping("/{uuidMoneyLocation}")
    public ResponseEntity<ResponseEnumMoneyLocationDTO> getByUuidMoneyLocation(
            @PathVariable(name = "uuidMoneyLocation") UUID uuidMoneyLocation) {
        logger.info("EnumMoneyLocationController: ---getByUuidMoneyLocation");
        return ResponseEntity.ok().body(moneyLocationService.getByUuidMoneyLocationDTI(uuidMoneyLocation));

    }
}
