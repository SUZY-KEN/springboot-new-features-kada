package com.example.samuraitravel.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImp1;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	
	public HouseController(HouseRepository houseRepository,ReviewRepository reviewRepository,UserRepository userRepository)
	{
		this.houseRepository=houseRepository;
		this.reviewRepository=reviewRepository;
		this.userRepository=userRepository;
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
	public String show(@PathVariable(name="id")Integer id,Model model,@AuthenticationPrincipal  UserDetailsImp1 userDetailsImp1) 
		
	{
		House house=houseRepository.getReferenceById(id);
		model.addAttribute("house",house);
		model.addAttribute("reservationInputForm",new ReservationInputForm());
		
		  
		  
		
			
				
		List<Review> reviewList=reviewRepository.findAllByHouse(houseRepository.getReferenceById(id));
		model.addAttribute("reviewList", reviewList);
		
		Boolean hasReview=true;


		List<Review>reviewPage= new ArrayList<Review>();
		System.out.println("REVIEWLISTインスタンス後"+reviewPage);
		
		if (userDetailsImp1 != null && userDetailsImp1.getUser() != null) {
	        User user = userRepository.getReferenceById(userDetailsImp1.getUser().getId());
	        
	        Review reviewUser=new Review();
	        for(Review review:reviewList)
	        {
	        	System.out.println("ユーザーれびゅー回し"+review);
	        	if(review.getUser()==user)
	        	{
	        		hasReview=false;
	        		
	        		reviewUser=review;
	        		reviewPage.add(review);
	        		System.out.println("reviewPageにユーザーレビュー追加"+review);
	        	
	        		 model.addAttribute("reviews", review);
	        	}
	        }
	       
	    	reviewList.remove(reviewUser);
    		System.out.println("reviewListのユーザーレビュー消去");
	    } 
		
		
		Integer i=0;
		if(!hasReview)
		{
			System.out.println("reviewPageにレビュー追加");
				for(Review review:reviewList)
				{
					i++;
					if(i>5)
					{
						break;
					}
					reviewPage.add(review);
					System.out.println("REVIEWPAGEADD前"+reviewPage);
				}
			
		}else
		{
			for(Review review:reviewList)
			{
				i++;
				if(i>6)
				{
					break;
				}
				reviewPage.add(review);
			}
		}

		
		System.out.println("りたーんまえ"+reviewPage);
		model.addAttribute("reviewPage",reviewPage);
		  model.addAttribute("hasReview",hasReview);
		
		return "houses/show";
	}

	
}	
	
	

