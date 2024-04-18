const stripe = Stripe('pk_test_51P4bLMRp9nH7sEwkcTs4wQhUT7rtR6aXGF1NntAL0I9TXmMpiBlocv56gRumtdYJqESMdlFagmNNqxPOMzHMTwva00X14JIKjr');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});
