package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.TariffPlanDTO;
import com.sharp.sharpshap.dto.RequestSearchByLineDTO;
import com.sharp.sharpshap.dto.ResponseTariffPlanDTO;
import com.sharp.sharpshap.service.TariffPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tariffPlan")
public class TariffPlanController {
    private final TariffPlanService tariffPlansService;
    private final static Logger logger = LoggerFactory.getLogger(TariffPlanController.class);

    @GetMapping
    public ResponseEntity<List<ResponseTariffPlanDTO>> getTariffPlanesDTOByKeyword(
            @Valid @RequestBody RequestSearchByLineDTO requestSearchByLineDTO) {
        logger.info("TariffPlanController ---getTariffPlanesDTOByKeyword ");

        return ResponseEntity.ok().body(
                tariffPlansService.getTariffPlansDTOByKeyword(requestSearchByLineDTO.getLineSearch())
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseTariffPlanDTO>> getAllTariffPlansDTO() {
        logger.info("TariffPlanController ---getAllTariffPlansDTO получение всех тарифныйх планов ");
        return ResponseEntity.ok().body(
                tariffPlansService.getAllTariffPlans()
        );
    }

    @PostMapping
    public ResponseEntity createTariffPlan(@Valid @RequestBody TariffPlanDTO createTariffPlanDTO) {
        logger.info("TariffPlanController ---createTariffPlan создание нового тарифного плана");
        tariffPlansService.createTariffPlan(createTariffPlanDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uuidTariffPlan}")
    public ResponseEntity<ResponseTariffPlanDTO> getByUuidTariffPlan(@PathVariable(name = "uuidTariffPlan") UUID uuidTariffPlan) {
        logger.info("TariffPlanController ---getByUuidTariffPlan получение тарифного плана по uuid");
        return ResponseEntity.ok().body(tariffPlansService.getByUuid(uuidTariffPlan));

    }

    @PatchMapping("/{uuidTariffPlan}")
    public ResponseEntity updateTariffPlan(@PathVariable(name = "uuidTariffPlan") UUID uuidTariffPlan,
                                           @Valid @RequestBody TariffPlanDTO tariffPlanDTO) {
        logger.info("TariffPlanController ---getByUuidTariffPlan изменение тарифного плана по uuid");

        tariffPlansService.updateTariffPlan(uuidTariffPlan, tariffPlanDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuidTariffPlan}")
    public ResponseEntity deleteTariffPlan(@PathVariable(name = "uuidTariffPlan") UUID uuidTariffPlan){
        logger.info("TariffPlanController ---deleteTariffPlan удаление тарифного плана по uuid");

        tariffPlansService.deleteTariffPlan(uuidTariffPlan);

        return ResponseEntity.ok().build();
    }

}
