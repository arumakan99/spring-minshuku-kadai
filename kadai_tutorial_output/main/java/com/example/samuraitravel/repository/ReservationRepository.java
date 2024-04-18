package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	// 戻り値：Page<Reservation>＝Reservationエンティティのリストがページングされた形で返される
	// 指定されたユーザーに関連する予約情報を作成日時の降順で取得し、結果をページ単位で取得する
	public Page<Reservation> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
