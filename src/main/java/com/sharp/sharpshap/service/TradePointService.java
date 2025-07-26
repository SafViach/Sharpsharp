package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseTradePointDTO;
import com.sharp.sharpshap.dto.ResponseTradePointsDTO;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.exceptions.TradePointNotFoundException;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.repository.TradePointRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TradePointService {
    private final TradePointRepository tradePointRepository;
    private static final Logger logger = LoggerFactory.getLogger(TradePointService.class);
    private final UserRepository userRepository;


    public TradePoint createTradePoint(TradePoint tradePoint) {
        return tradePointRepository.save(tradePoint);
    }

    @Transactional
    public ResponseTradePointDTO setTradePointForUser(UUID uuidUser, UUID uuidTradePoint) {
        logger.info("TradePointService: ---setTradePointForUser присваиваем пользователю точку на которой он работает");
        User user = userRepository.findById(uuidUser).orElseThrow(() ->
                new UsernameNotFoundException("TradePointService: ---setTradePointForUser Пользователь не найден"));

        TradePoint tradePoint = tradePointRepository.findById(uuidTradePoint).orElseThrow(() ->
                new TradePointNotFoundException("TradePointService: ---setTradePointForUser Торговая точка не найдена"));

        ResponseTradePointDTO responseTradePointDTO = new ResponseTradePointDTO();
        responseTradePointDTO.setTradePoint(tradePoint);
        user.setTradePointId(tradePoint);

        return responseTradePointDTO;
    }


    public TradePoint getByIdTradePoint(UUID uuidUser, UUID uuidTradePoint) {
        logger.info("TradePointService: ---getByIdTradePoint ищем торговую точку по uuid");
        return tradePointRepository.findById(uuidTradePoint).orElseThrow(() -> new TradePointNotFoundException("Такой точки с id не найдена"));

    }

    public TradePoint getByIdTradePoint(UUID uuidUser) {
        return tradePointRepository.findById(uuidUser).orElseThrow(() -> new TradePointNotFoundException("Такой точки с id не найдена"));
    }

    public ResponseTradePointsDTO getAllTradePoints() {
        logger.info("TradePointService: ---getAllTradePoints Поиск всех торговых точек в БД");
        List<TradePoint> tradePoint = tradePointRepository.findAll();
        if (tradePoint.isEmpty()) {
            throw new RuntimeException("Список точек пуст");
        }

        ResponseTradePointsDTO responseTradePointsDTO = new ResponseTradePointsDTO();
        responseTradePointsDTO.setTradePoints(tradePoint);
        return responseTradePointsDTO;
    }

    public TradePoint updateTradePoint(UUID uuid, TradePoint updateTradePoint) {
        TradePoint oldTradePoint = tradePointRepository.findById(uuid)
                .orElseThrow(() -> new TradePointNotFoundException("Такой точки с id = " + uuid + " не найден"));
        oldTradePoint.setName(updateTradePoint.getName());
        oldTradePoint.setAddress(updateTradePoint.getAddress());
        oldTradePoint.setMoneyInTheCashRegister(updateTradePoint.getMoneyInTheCashRegister());
        oldTradePoint.setMoneyInBox(updateTradePoint.getMoneyInBox());

        return tradePointRepository.save(oldTradePoint);
    }

    public void deleteByIdTradePoint(UUID id) {
        tradePointRepository.deleteById(id);
    }
}
