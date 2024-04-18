package com.example.samuraitravel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;  
    private final HouseRepository houseRepository;  
    private final UserRepository userRepository;  
    
    public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;  
        this.houseRepository = houseRepository;  
        this.userRepository = userRepository;  
    }    
    
    @Transactional
    public void create(Map<String, String> paymentIntentObject) {
        // 新しい予約オブジェクトを作成
        Reservation reservation = new Reservation();
        
        Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
        Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));
        
        // 民宿情報とユーザー情報を取得
        House house = houseRepository.getReferenceById(houseId);       
        User user = userRepository.getReferenceById(userId);
        // チェックイン日とチェックアウト日をパース
        LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
        LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
        Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));        
        Integer amount = Integer.valueOf(paymentIntentObject.get("amount")); 
                
        // 予約情報をセット
        reservation.setHouse(house);
        reservation.setUser(user);
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setAmount(amount);
        
        // 予約を保存
        reservationRepository.save(reservation);
    }  
    
    // 宿泊人数が定員以下かどうかをチェックする
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        return numberOfPeople <= capacity;
    }

    // 宿泊料金を計算する
    public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
        // チェックイン日からチェックアウト日までの泊数を計算
        long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        // 泊数に宿泊料金をかけて合計金額を計算
        int amount = price * (int) numberOfNights;
        return amount;
    }    
}