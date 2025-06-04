package com.sharp.sharpshap.service;

import com.sharp.sharpshap.exceptions.TradePointNotFoundException;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.repository.TradePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TradePointService {
    private TradePointRepository tradePointRepository;

    @Autowired
    public void setTradePointRepository(TradePointRepository tradePointRepository) {
        this.tradePointRepository = tradePointRepository;
    }

    public TradePoint createTradePoint(TradePoint tradePoint){
        return tradePointRepository.save(tradePoint);
    }


    public TradePoint getByIdTradePoint(int id){
        return tradePointRepository.findById(id).orElseThrow(()-> new TradePointNotFoundException("Такой точки с id = "+ id +" не найден"));
    }

    public List<TradePoint> getAllTradePoints(){
        List<TradePoint> tradePoint = tradePointRepository.findAll();
        if(tradePoint.isEmpty()){
            throw new RuntimeException("Список точек пуст");
        }
        return tradePoint;
    }

    public TradePoint updateTradePoint(int id , TradePoint updateTradePoint){
        TradePoint oldTradePoint = tradePointRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Такой точки с id = "+ id +" не найден"));
        oldTradePoint.setName(updateTradePoint.getName());
        oldTradePoint.setAddress(updateTradePoint.getAddress());
        oldTradePoint.setMoneyInTheCashRegister(updateTradePoint.getMoneyInTheCashRegister());
        oldTradePoint.setMoneyInBox(updateTradePoint.getMoneyInBox());

        return tradePointRepository.save(oldTradePoint);
    }
    public void deleteByIdTradePoint(int id){
        tradePointRepository.deleteById(id);
    }
}
