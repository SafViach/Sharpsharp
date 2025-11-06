package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.TariffPlanDTO;
import com.sharp.sharpshap.dto.ResponseTariffPlanDTO;
import com.sharp.sharpshap.entity.TariffPlan;
import com.sharp.sharpshap.exceptions.TariffPlaneException;
import com.sharp.sharpshap.repository.TariffPlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffPlanService {
    private final TariffPlanRepository tariffPlanRepository;
    private final static Logger logger = LoggerFactory.getLogger(TariffPlanService.class);

    private TariffPlan getTariffPlansByUuid(UUID uuidTariffPlane) {
        return tariffPlanRepository.findById(uuidTariffPlane).orElseThrow(() ->
                new TariffPlaneException("TariffPlanService ---getByUuid тарифный план по uuid не найден в БД"));
    }

    public List<TariffPlan> getTariffPlansByKeyword(String keyword) {
        logger.info("TariffPlanService ---getTariffPlansByKeyword поиск по keyword: " + keyword);

        String cleaner = keyword.trim().toLowerCase();
        String[] parts = cleaner.split("\\s+");
        logger.info("TariffPlanService ---getTariffPlansByKeyword keyword содержет : " + parts.length + " слог");

        if (parts.length == 1) {
            return tariffPlanRepository.findByKeyword(parts[0]);
        } else if (parts.length == 2) {
            return tariffPlanRepository.findByKeywordTwoParts(parts[0], parts[1]);
        } else if (parts.length == 3) {
            return tariffPlanRepository.findByKeywordThreeParts(parts[0], parts[1], parts[2]);
        } else if (parts.length == 4) {
            return tariffPlanRepository.findByKeywordFourParts(parts[0], parts[1], parts[2], parts[3]);
        }
        return tariffPlanRepository.findByKeywordFourParts(parts[0], parts[1], parts[2], parts[3]);
    }

    public List<ResponseTariffPlanDTO> getTariffPlansDTOByKeyword(String keyword) {
        return getTariffPlansByKeyword(keyword).stream()
                .map(tariffPlan -> ResponseTariffPlanDTO.toDTO(tariffPlan))
                .collect(Collectors.toList());
    }

    public List<ResponseTariffPlanDTO> getAllTariffPlans() {
        return tariffPlanRepository.findAll().stream()
                .map(tariffPlan -> ResponseTariffPlanDTO.toDTO(tariffPlan))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTariffPlan(TariffPlanDTO createTariffPlanDTO) {
        logger.info("TariffPlanService ---createTariffPlan создание нового тарифного плана : " +
                createTariffPlanDTO.getName() + " " + createTariffPlanDTO.getPrice());
        existsByName(createTariffPlanDTO.name, createTariffPlanDTO.price);
        TariffPlan tariffPlan = new TariffPlan();
        tariffPlan.setName(createTariffPlanDTO.name.trim());
        tariffPlan.setPrice(createTariffPlanDTO.price);

        tariffPlanRepository.save(tariffPlan);
    }

    public ResponseTariffPlanDTO getByUuid(UUID uuidTariffPlan) {
        TariffPlan tariffPlan = getTariffPlansByUuid(uuidTariffPlan);

        logger.info("TariffPlanService ---getByUuid получение по uuid тарифный план: " + tariffPlan.getName() +
                " " + tariffPlan.getPrice());
        return ResponseTariffPlanDTO.toDTO(tariffPlan);
    }



    @Transactional
    public void updateTariffPlan(UUID uuidTariffPlan, TariffPlanDTO updateTariffPlanDTO) {
        TariffPlan tariffPlan = getTariffPlansByUuid(uuidTariffPlan);
        logger.info("TariffPlanService ---updateTariffPlan изменение тарифного плана: " + tariffPlan.getName() +
                " " + tariffPlan.getPrice() + " на: " + updateTariffPlanDTO.getName() + " " +
                updateTariffPlanDTO.getPrice());

        existsByName(updateTariffPlanDTO.name, updateTariffPlanDTO.price);

        tariffPlan.setName(updateTariffPlanDTO.name);
        tariffPlan.setPrice(updateTariffPlanDTO.price);

        tariffPlanRepository.save(tariffPlan);

    }

    public void existsByName(String name, BigDecimal price) {
        logger.info("TariffPlanService ---existsByName проверяем есть ли данный обьект уже в БД ");
        if (tariffPlanRepository.existsByNameAndPrice(name.trim(), price)) {
            throw new TariffPlaneException("Тарифный план с именем: " + name +
                    " с данной суммой " + price + " присутствует в БД");
        }
    }

    @Transactional
    public void deleteTariffPlan(UUID uuidTariffPlan) {
        TariffPlan tariffPlan = getTariffPlansByUuid(uuidTariffPlan);
        logger.info("TariffPlanService ---deleteTariffPlan удаление по uuid тарифный план: " + tariffPlan.getName() +
                " " + tariffPlan.getPrice());
        tariffPlanRepository.delete(tariffPlan);
    }
}
