package uz.geeks.stripeintegration.controller

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uz.geeks.stripeintegration.utils.CredentialsUtils
import uz.geeks.stripeintegration.dto.CreatePayment
import uz.geeks.stripeintegration.dto.CreatePaymentResponse

@RestController
class ServerController {

    val stripeApiKey: String? get() = CredentialsUtils().getStripeApiKey()

    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(
        @RequestBody createPayment: CreatePayment
    ): CreatePaymentResponse {

        Stripe.apiKey = stripeApiKey

        return try{
            val amount = createPayment.amount.times(100.toBigDecimal())
            val params = PaymentIntentCreateParams.builder()
                .setAmount(amount.toLong())
                .setCurrency("aed")
                .addAllPaymentMethodType(
                    listOf(
                        "card"))
                .build()

            val paymentIntent = PaymentIntent.create(params)

            val createPaymentResponse = CreatePaymentResponse(paymentIntent.clientSecret)

            CreatePaymentResponse(createPaymentResponse.clientSecret)

        } catch (e: Exception) {
            e.printStackTrace()
            CreatePaymentResponse("")
        }

    }
}