package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseEnumMoneyLocationDTO;
import com.sharp.sharpshap.enums.EnumMoneyLocation;
import com.sharp.sharpshap.exceptions.EnumMoneyLocationException;
import com.sharp.sharpshap.repository.EnumMoneyLocationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnumMoneyLocationService {
    private final EnumMoneyLocationRepository moneyLocationRepository;
    private final Logger logger = LoggerFactory.getLogger(EnumMoneyLocationService.class);

    private List<EnumMoneyLocation> getAllMoneyLocation(){
        logger.info("EnumMoneyLocationService: ---getAllMoneyLocation() ");
        return moneyLocationRepository.findAll();
    }

    private EnumMoneyLocation getByUuidMoneyLocation(UUID uuidMoneyLocation){
        logger.info("EnumMoneyLocationService: ---getByUuidMoneyLocation ");
        return moneyLocationRepository.findById(uuidMoneyLocation).orElseThrow(()->
                new EnumMoneyLocationException("EnumMoneyLocationService: ---getByUuidMoneyLocation путь по uuid не найден"));
    }

    public List<ResponseEnumMoneyLocationDTO> getAllMoneyLocationDTO(){
        logger.info("EnumMoneyLocationService: ---getAllMoneyLocationDTO() ");
        return getAllMoneyLocation().stream()
                .map(moneyLocation -> ResponseEnumMoneyLocationDTO.toDTO(moneyLocation))
                .collect(Collectors.toList());
    }

    public ResponseEnumMoneyLocationDTO getByUuidMoneyLocationDTI(UUID uuidMoneyLocation){
        logger.info("EnumMoneyLocationService: ---getByUuidMoneyLocationDTI ");
        return ResponseEnumMoneyLocationDTO.toDTO(getByUuidMoneyLocation(uuidMoneyLocation));
    }


}
