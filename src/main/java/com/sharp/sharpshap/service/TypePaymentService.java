package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseTypePaymentDTO;
import com.sharp.sharpshap.enums.EnumTypePayment;
import com.sharp.sharpshap.exceptions.TypePaymentException;
import com.sharp.sharpshap.repository.TypePaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypePaymentService {
    private final TypePaymentRepository typePaymentRepository;
    private static final Logger logger = LoggerFactory.getLogger(TypePaymentService.class);

    private EnumTypePayment getTypePaymentByUuid(UUID uuidTypePayment) {
        logger.info("TypePaymentService: ---getTypePaymentByUuid");
        return typePaymentRepository.findById(uuidTypePayment).orElseThrow(
                () -> new TypePaymentException("TypePaymentService ---getTypePaymentByUuid тип опланы в БД по uuid не найден"));
    }

    public ResponseTypePaymentDTO getTypePaymentDTOByUuid(UUID uuidTypePayment) {
        return ResponseTypePaymentDTO.toDTO(getTypePaymentByUuid(uuidTypePayment));
    }

    public List<ResponseTypePaymentDTO> getAllTypePaymentsDTO() {
        return typePaymentRepository.findAll().stream()
                .map(enumTypePayment -> ResponseTypePaymentDTO.toDTO(enumTypePayment))
                .collect(Collectors.toList());
    }
}
