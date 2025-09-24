package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseNameTradePointDTO;
import com.sharp.sharpshap.dto.ResponseTradePointForAuthDTO;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public void setTradePointForUser(Object uuidUser, UUID uuidTradePoint) {
        if (uuidUser == null){
            throw  new UsernameNotFoundException("TradePointService: --- В метод setTradePointForUser uuidUser == null");
        }
        User user = userRepository.findById(UUID.fromString(uuidUser.toString())).orElseThrow(() ->
                new UsernameNotFoundException("TradePointService: ---setTradePointForUser Пользователь не найден"));

        TradePoint tradePoint = tradePointRepository.findById(uuidTradePoint).orElseThrow(() ->
                new TradePointNotFoundException("TradePointService: ---setTradePointForUser Торговая точка не найдена"));
        logger.info("TradePointService: ---setTradePointForUser присваиваем пользователю " + user.getFirstName() + " " + user.getLastName() +
                "точку на которой он работает: " + tradePoint.getName());
        user.setTradePointId(tradePoint);
        userRepository.save(user);
    }


    public TradePoint getByIdTradePoint(UUID uuidUser, UUID uuidTradePoint) {
        logger.info("TradePointService: ---getByIdTradePoint ищем торговую точку по uuid");
        return tradePointRepository.findById(uuidTradePoint).orElseThrow(() -> new TradePointNotFoundException("Такой точки с id не найдена"));

    }

    public TradePoint getByIdTradePoint(UUID uuidTradePoint) {
        return tradePointRepository.findById(uuidTradePoint).orElseThrow(() -> new TradePointNotFoundException("Такой точки с id не найдена"));
    }

    public List<ResponseTradePointForAuthDTO> getTradePointsForAuth(){
        return transformResponseTradePointForAuth(getAllTradePoints());
    }
    private List<ResponseTradePointForAuthDTO> transformResponseTradePointForAuth(List<TradePoint> tradePoints){
        return tradePoints.stream()
                .map(tradePoint -> new ResponseTradePointForAuthDTO(
                        tradePoint.getId(),
                        tradePoint.getName(),
                        tradePoint.getAddress(),
                        tradePoint.getMoneyInBox(),
                        tradePoint.getMoneyInTheCashRegister(),
                        tradePoint.getSumFinishOffTheMoney()
                ))
                .collect(Collectors.toList());
    }

    public List<TradePoint> getAllTradePoints() {
        logger.info("TradePointService: ---getAllTradePoints получение всех торговых точек в БД");
        return tradePointRepository.findAll();
    }
    public List<ResponseNameTradePointDTO> getAllTradePointResponseNameTradePointDTO(){
        return transInResponseNameTradePointDTO(getAllTradePoints());
    }

    private List<ResponseNameTradePointDTO> transInResponseNameTradePointDTO(List<TradePoint> tradePoints){
        return tradePoints.stream()
                .map( tradePoint -> new ResponseNameTradePointDTO(tradePoint.getId(), tradePoint.getName()))
                .collect(Collectors.toList());
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
