package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImp1;
import com.example.samuraitravel.service.FavoriteService;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	
	
	public HouseController(HouseRepository houseRepository,ReviewRepository reviewRepository,UserRepository userRepository,FavoriteRepository favoriteRepository,FavoriteService favoriteService)
	{
		
		this.houseRepository=houseRepository;
		this.reviewRepository=reviewRepository;
		this.userRepository=userRepository;
		this.favoriteRepository=favoriteRepository;
		this.favoriteService=favoriteService;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@GetMapping
	public String index(@RequestParam(name="keyword" ,required=false)String keyword,
			@RequestParam(name="area",required=false)String area,
			@RequestParam(name="price",required=false)Integer price,
			@RequestParam(name="order",required=false)String order,
			@PageableDefault(page=0,size=10,sort="id",direction=Direction.ASC)Pageable pageable,
			Model model)
	{
		
		Page<House>housePage;
		
		if(keyword!=null&&!keyword.isEmpty())
		{
			if(order!=null&&order.equals("priceAsc"))
			{
				housePage=houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%"+keyword+"%", "%"+keyword+"%", pageable);
			}
			else
			{
				housePage=houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%"+keyword+"%", "%"+keyword+"%", pageable);
			}
			
		}
		else if(area!=null&&!area.isEmpty())
		{
			
			if(order!=null&&order.equals("priceAsc"))
			{
				housePage=houseRepository.findByAddressLikeOrderByPriceAsc( "%"+area+"%", pageable);
			}
			else
			{
				housePage=houseRepository.findByAddressLikeOrderByCreatedAtDesc("%"+area+"%", pageable);
			}
			
			
		}
		
		else if(price!=null)
		{
			if(order!=null&&order.equals("priceAsc"))
			{
				housePage=houseRepository.findByPriceLessThanEqualOrderByPriceAsc( price, pageable);
			}
			else
			{
				housePage=houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc( price, pageable); 
			}
			
		}
		
		else
		{
			if(order!=null&&order.equals("priceAsc"))
			{
				housePage=houseRepository.findAllByOrderByPriceAsc(pageable);
			}
			else
			{
				housePage=houseRepository.findAllByOrderByCreatedAtDesc(pageable); 
			}
		}
		
		  model.addAttribute("housePage", housePage);
	         model.addAttribute("keyword", keyword);
	         model.addAttribute("area", area);
	         model.addAttribute("price", price);  
	         model.addAttribute("order",order);
		
		return "houses/index";
	}
	
	
	
	
	
	
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name="id")Integer id,Model model,@PageableDefault(page=0,size=6,sort="id",direction = Direction.ASC)Pageable pageable,@AuthenticationPrincipal  UserDetailsImp1 userDetailsImp1) 
		
	{
		House house=houseRepository.getReferenceById(id);
		model.addAttribute("house",house);
		model.addAttribute("reservationInputForm",new ReservationInputForm());
		
		  
		
		//houses/show.htmlのページネーションの設定
		
		Page<Review> reviewPage=reviewRepository.findAllByHouse(houseRepository.getReferenceById(id),pageable);
		model.addAttribute("reviewPage", reviewPage);
		
		Boolean hasReview=true;

		  if (userDetailsImp1 != null && userDetailsImp1.getUser() != null) {
			  
			  //ユーザー毎のこの民宿にレビューがあるかの判定
		        User user = userRepository.getReferenceById(userDetailsImp1.getUser().getId());
		        for(Review review:reviewPage)
		        {
		        	
		        	if(review.getUser()==user)
		        	{
		        		hasReview=false;
		        		
		        		 model.addAttribute("reviews", review);
		        	}
		        }
		        
		      //ユーザー毎のこの民宿をお気に入り登録しているか判定
				Favorite favorite=favoriteRepository.findAllByHouseAndUserId(house,user.getId());		       
				model.addAttribute("favorite",favorite);
				model.addAttribute("user",user);
				
		    } 

		  model.addAttribute("hasReview",hasReview);
		  
		
		  
			  
		return "houses/show";
	}

	@GetMapping("/{houseId}/favorize/{userId}")
	public String favorize(@PathVariable(name="userId")Integer userId,@PathVariable(name="houseId")Integer houseId)
	{
		
		System.out.println("favorize");
		House house=houseRepository.getReferenceById(houseId);
		favoriteService.createFavorite(userId, house);
		return"redirect:/houses/{houseId}";
	}
	
	@GetMapping("/{houseId}/disfavorize/{id}")
	public String disfavorize(@PathVariable(name="id")Integer id)
	{
		System.out.println("disfavorize");
		
		
		favoriteRepository.deleteById(id);
		return"redirect:/houses/{houseId}";
	}
	
}	
	
	

