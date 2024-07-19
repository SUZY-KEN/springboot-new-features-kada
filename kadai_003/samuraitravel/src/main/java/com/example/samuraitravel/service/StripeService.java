package com.example.samuraitravel.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {

	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private final ReservationService reservationService;
	
	public StripeService(ReservationService reservationService)
	{
		this.reservationService=reservationService;
	}
	
	
	
	public String createStripeSession(String houseName,ReservationRegisterForm reservationRegisterForm,HttpServletRequest httpServletRequest)
	{
		
		
		
		  // セッションを作成し、Stripeに必要な情報を返す
	         Stripe.apiKey = stripeApiKey;
	         String requestUrl = new String(httpServletRequest.getRequestURL());
	         SessionCreateParams params =
	             SessionCreateParams.builder()
	                 .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
	                 .addLineItem(
	                     SessionCreateParams.LineItem.builder()
	                         .setPriceData(
	                             SessionCreateParams.LineItem.PriceData.builder()   
	                                 .setProductData(
	                                     SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                         .setName(houseName)
	                                         .build())
	                                 .setUnitAmount((long)reservationRegisterForm.getAmount())
	                                 .setCurrency("jpy")                                
	                                 .build())
	                         .setQuantity(1L)
	                         .build())
	                 .setMode(SessionCreateParams.Mode.PAYMENT)
	                 .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
	                 .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
	                 .setPaymentIntentData(
	                     SessionCreateParams.PaymentIntentData.builder()
	                         .putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
	                         .putMetadata("userId", reservationRegisterForm.getUserId().toString())
	                         .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
	                         .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
	                         .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
	                         .putMetadata("amount", reservationRegisterForm.getAmount().toString())
	                         .build())
	                 .build();
	         try {
	             Session session = Session.create(params);
	             return session.getId();
	         } catch (StripeException e) {
	             e.printStackTrace();
	             return "";
	         }
	         

	}
				
	     		
         public void processSessionCompleted(Event event) {
        	 
//        	 StripeObjectオブジェクト＝StripeのAPIから返されるデータを表現する基本的なオブジェクト
//        	 Eventオブジェクト＝Stripeから通知されるイベントの内容を表現したオブジェクト
        	 
        	 
//        	 イベントオブジェクトからStripeObjectオブジェクトを取得
//        	 Optionalとは、nullを持つ可能性のあるオブジェクトを扱うためのクラス
        	 Optional<StripeObject>optionalStripeObject=event.getDataObjectDeserializer().getObject();
        	 
        	 System.out.println(optionalStripeObject);
//        	 Optionalクラスが提供するifPresent()メソッドを使うことで、以下のように簡潔にnullチェック
        	 optionalStripeObject.ifPresentOrElse(stripeObject->{
        		 
//        		 StripeObjectオブジェクトをSessionオブジェクトに変換
        		 Session session=(Session)stripeObject;	 
        		 
//        		 "payment_intent"情報を展開する（詳細情報を含める）ように指定したSessionRetrieveParamsオブジェクトを生成
        		 SessionRetrieveParams params=SessionRetrieveParams.builder().addExpand("payment_intent").build();
        		 System.out.println("param:"+params);
        		 System.out.println("session:"+session);
        		 try {
        			 System.out.println("aaaaa");
        			 session=Session.retrieve(session.getId(),params,null);
        			 System.out.println(session.getId());
        			 System.out.println(params);
        			 Map<String,String>paymentIntentObject=session.getPaymentIntentObject().getMetadata();
        			 System.out.println(paymentIntentObject);
        			 reservationService.create(paymentIntentObject);
        			 
        		 }catch(StripeException e) {
        			 e.printStackTrace();
        		 }
        				
        		   System.out.println("予約一覧ページの登録処理が成功しました。");
                   System.out.println("Stripe API Version: " + event.getApiVersion());
                   System.out.println("stripe-java Version: " + Stripe.VERSION);
        	 
        	 },
        	()->{
        		 System.out.println("予約一覧ページの登録処理が失敗しました。");
                 System.out.println("Stripe API Version: " + event.getApiVersion());
                 System.out.println("stripe-java Version: " + Stripe.VERSION);
         });
         
         

 		
         
     }
}
