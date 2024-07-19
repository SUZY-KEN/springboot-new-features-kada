//公開キーの宣言
const stripe = window.Stripe('pk_test_51PaX7XCj3ORFf5wgQ0G39TjDGKfJEqM4aiABVKDZfMsuvj5Ko97jSosXhHdK3VtuRoJfPBll7TA6DFcRuGLkzZLC00fdXTFt6R');
 const paymentButton = document.querySelector('#paymentButton');

 paymentButton.addEventListener('click', () => {
	 console.log("xxx");
	 
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });
