package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImp1;


@Controller
public class FavoriteContoroller {
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteContoroller(UserRepository userRepository, FavoriteRepository favoriteRepository) 
	{
		this.userRepository=userRepository;
		this.favoriteRepository=favoriteRepository;
	}

	@GetMapping("/favorites")
	public String favorite(@AuthenticationPrincipal UserDetailsImp1 userDetailsImp1,@PageableDefault(page=0,size=10,sort="createdAt",direction = Direction.DESC)Pageable pageable,Model model) 
	{
		User user=userRepository.getReferenceById(userDetailsImp1.getUser().getId());
		Page<Favorite> favoritePage=favoriteRepository.findAllByUserId(user.getId(),pageable);
		
		
		model.addAttribute("favoritePage",favoritePage);
		return "favorites/index";
	}
	
}
