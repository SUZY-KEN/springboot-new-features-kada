package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.samuraitravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {
	private final StripeService stripeService;
	
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	@Value("${stripe.webhook-secret}")
	private String webhookSecret;
	
	public StripeWebhookController(StripeService stripeService)
	{
		this.stripeService=stripeService;
	}
	
//	Webhookイベントの通知先をlocalhost:8080/stripe/webhookとコマンドで設定した
//	PostMappingで上記と対応させる
	@PostMapping("/stripe/webhook")
	
//	@RequestBody、@RequestHeaderはPostで送る情報のボディーの部分、ヘッダーの部分の各項目をそのまま取得することができる
	public ResponseEntity<String>webhook(@RequestBody String payload,@RequestHeader("Stripe-Signature")String sigHeader)
	{
		Stripe.apiKey=stripeApiKey;
		
//		イベントが毎回TRY-CATCHで判定されるようにnullにしておく。
		Event event=null;
		System.out.println("イベントが毎回TRY-CATCHで判定される");	
		try {
			System.out.println(Webhook.constructEvent(payload, sigHeader, webhookSecret));	
			event=Webhook.constructEvent(payload, sigHeader, webhookSecret);
		}catch(SignatureVerificationException e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		
		}
		
//		Stripeから受信できるイベント"checkout.session.completed"を正しく取得できているかの判定
		if("checkout.session.completed".equals(event.getType()))
		{
			
			System.out.println("Stripeから受信できるイベント\"checkout.session.completed\"を正しく取得できている");	
			stripeService.processSessionCompleted(event);
		}
		
//		HTTPのステータスコード（＝リクエストに対するレスポンスの状態（成功・失敗など）を3桁の数値で表したもの）を返す
		return new ResponseEntity<>("Success",HttpStatus.OK);
		
//		予約一覧ページへのリダイレクトはStripe側でおこなってくれる
	}

}
