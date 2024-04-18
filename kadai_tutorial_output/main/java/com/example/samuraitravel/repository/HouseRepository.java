package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

// JpaRepositoryインターフェース継承により、基本的なCRUD操作を行うためのメソッドが利用可能に
// インターフェースにはメソッドの名前、引数の型、戻り値の型のみ定義（メソッドの具体的な処理内容は記述しない）
// JpaRepository<エンティティのクラス型, 主キーのデータ型>
public interface HouseRepository extends JpaRepository<House, Integer> {
	// nameカラムに特定のkeywordが含まれるHouseエンティティを検索し結果をPageオブジェクトとして返す
	// Pageableオブジェクトを引数として受け取り、ページングやソートなどの情報を処理する
	public Page<House> findByNameLike(String keyword, Pageable pageable);
	
	// 民宿名または目的地で検索する（新着順）
    public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    // 民宿名または目的地で検索する（宿泊料金が安い順）
    public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    // エリアで検索する（新着順）
    public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
    // エリアで検索する（宿泊料金が安い順）
    public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
    // 1泊あたりの予算で検索する（新着順）
    public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
    // 1泊あたりの予算で検索する（宿泊料金が安い順）
    public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable); 
    // すべてのデータを取得する（新着順）
    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);
   // すべてのデータを取得する（宿泊料金が安い順）
    public Page<House> findAllByOrderByPriceAsc(Pageable pageable);
    
    public List<House> findTop10ByOrderByCreatedAtDesc();
}
