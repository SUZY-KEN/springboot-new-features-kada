package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;

public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {

	public Favorite findAllByHouseAndUserId(House house,Integer userId);
	public Page<Favorite> findAllByUserId(Integer userId,Pageable pageable);
	
}
