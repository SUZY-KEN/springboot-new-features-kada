package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImp1;
import com.example.samuraitravel.service.ReviewService;




@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private final ReviewService reviewService;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	
	

	public ReviewController(ReviewService reviewService,HouseRepository houseRepository,UserRepository userRepository,ReviewRepository reviewRepository)
	{
		
		this.houseRepository=houseRepository;
		this.reviewService=reviewService;
		this.userRepository=userRepository;
		this.reviewRepository=reviewRepository;
	}
	

//  houses/show.htmlのボタンから
//	登録フォーム
	@GetMapping("create/{id}")
	public String create(@PathVariable(name="id")Integer id,Model model,@AuthenticationPrincipal  UserDetailsImp1 userDetailsImp1)
	{
		
		House house=houseRepository.getReferenceById(id);
		model.addAttribute("house",house);
		 User user=userRepository.getReferenceById(userDetailsImp1.getUser().getId());
		ReviewForm reviewForm=new ReviewForm(null,user.getId(), house.getId(), null, null);
		System.out.println(reviewForm);

		System.out.println("ready");
		model.addAttribute("reviewForm",reviewForm);
		
		return "/review/create";
	}
	
	@GetMapping("edit/{id}")
	public String edit(@PathVariable(name="id")Integer id,Model model)
	{
		
		Review review=reviewRepository.getReferenceById(id);
		
		
		
		ReviewForm reviewForm=new ReviewForm(review.getId(),review.getUser().getId(), review.getHouse().getId(), review.getStarNum(), review.getPostedComment());
		System.out.println(reviewForm);
		model.addAttribute("reviewForm",reviewForm);
		model.addAttribute("house",review.getHouse());
		
		return "/review/edit";
	}
	


	
//	登録ポスト
	@PostMapping("create/{id}/register")
	public String registerCreate(@PathVariable(name="id") Integer id, @ModelAttribute @Validated ReviewForm reviewForm,BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model)
	{
		
		System.out.println("create");
		
		if(bindingResult.hasErrors())
		{
			redirectAttributes.addFlashAttribute("errorMessage","コメント欄が未記入です");

			return "redirect:/review/create/"+id;
		}
		
		
		reviewService.createReview(reviewForm);
		redirectAttributes.addFlashAttribute("successMessage","レビューが正常に登録されました");
		
		return "redirect:/review/show/{id}";
	}
	
//	登録ポスト
	@PostMapping("edit/{id}/register")
	public String registerEdit (@PathVariable(name="id") Integer id,@ModelAttribute @Validated ReviewForm reviewForm,BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model)
	{
		
		System.out.println("edit");
		
		if(bindingResult.hasErrors())
		{
			redirectAttributes.addFlashAttribute("errorMessage","コメント欄が未記入です");

			return "redirect:/review/edit/"+id;
		}
		System.out.println(reviewForm.getUserId());
		reviewService.updateReview(reviewForm);
		redirectAttributes.addFlashAttribute("successMessage","レビューが正常に編集されました");
		
		return "redirect:/review/show/"+reviewForm.getHouseId();
	}
	
//	コメント詳細
	@GetMapping("/show/{id}")
	public String show(@PathVariable(name="id")Integer id,Model model,@PageableDefault(page=0,size=10,sort="id",direction = Direction.ASC)Pageable pageable,@AuthenticationPrincipal  UserDetailsImp1 userDetailsImp1)
	{
		House house=houseRepository.getReferenceById(id);
		Page<Review> reviewPage=reviewRepository.findAllByHouse(houseRepository.getReferenceById(id),pageable);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("house",house);
		
		return "/review/show";
	}
	
//	レビューモーダル削除
	 @PostMapping("/{id}/{houseId}/delete")
	    
	    public String delete(@PathVariable(name="id")Integer id,@PathVariable(name="houseId")Integer houseId,RedirectAttributes redirectAttributes)
	    {
	    	reviewRepository.deleteById(id);
	    
	    	redirectAttributes.addFlashAttribute("succesMessage","民宿を削除しました");
	    	return "redirect:/houses/{houseId}";
	    	
	    }
	
}
