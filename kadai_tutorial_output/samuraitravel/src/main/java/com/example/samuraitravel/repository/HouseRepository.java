package com.example.samuraitravel.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;

public interface HouseRepository extends JpaRepository<House, Integer> {
	public Page<House>findByNameLike(String keyword,Pageable pageable);
	
	
	//HouseCOntrollerで使用する
	//findAAAorderbyBBBAsc(or)Desc
	//AAAには検索対象、BBBには並び替え対象カラム
	public Page<House>findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyward,String addressKeyward,Pageable pageable);
	public Page<House>findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyward,String addressKeyward,Pageable pageable);
	public Page<House>findByAddressLikeOrderByCreatedAtDesc(String area,Pageable pageable);
	public Page<House>findByAddressLikeOrderByPriceAsc(String area,Pageable pageable);
	public Page<House>findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price,Pageable pageable);
	public Page<House>findByPriceLessThanEqualOrderByPriceAsc(Integer price,Pageable pageable);
	public Page<House>findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<House>findAllByOrderByPriceAsc(Pageable pageable);
	
//	HomeControllerで使用する
	
//	TOP○○はSQLのLIMIT句と同様に取得するデータの数を制限できる。
	public List<House>findTop10ByOrderByCreatedAtDesc();
}
