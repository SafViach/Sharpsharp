package com.sharp.sharpshap.service;

import com.sharp.sharpshap.entity.Discount;
import com.sharp.sharpshap.exceptions.DiscountNotFoudException;
import com.sharp.sharpshap.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;

    public Discount getByAmountDiscount(BigDecimal amountDiscount){
        return discountRepository.findByDiscountAmount(amountDiscount).orElseThrow(
                ()-> new DiscountNotFoudException("Скидка не найдена")
        );
    }
}
