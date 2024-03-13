package uz.geeks.stripeintegration.controller

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uz.geeks.stripeintegration.dto.CreatePayment
import uz.geeks.stripeintegration.dto.CreatePaymentResponse

@RestController
class ServerController {

    @Value("\${stripe.api.key}")
    private val stripeApiKey: String = ""

    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(
        @RequestBody createPayment: CreatePayment
    ): CreatePaymentResponse {

        Stripe.apiKey = stripeApiKey

        val amount = createPayment.amount * 100.toBigDecimal()

        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount.toLong())
            .setCurrency("aed")
            .addAllPaymentMethodType(
                listOf(
                    "card"))
            .build()

        val paymentIntent = PaymentIntent.create(params)
        val createPaymentResponse = CreatePaymentResponse(paymentIntent.clientSecret)

        println("createPaymentResponse => ${createPaymentResponse.clientSecret}")

        return createPaymentResponse
    }
}