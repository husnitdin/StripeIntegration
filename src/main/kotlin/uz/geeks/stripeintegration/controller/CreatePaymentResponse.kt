package uz.geeks.stripeintegration.controller

import com.fasterxml.jackson.databind.json.JsonMapper
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

        println(JsonMapper().writeValueAsString(createPayment.amount))

        val params = PaymentIntentCreateParams.builder()
            .setAmount(createPayment.amount.toLong()) // Stripe accepts as Cents
            .setCurrency("aed")
            .addAllPaymentMethodType(
                listOf(
                    "card"))
            .build()

        println("param => $params")

        val paymentIntent = PaymentIntent.create(params)
        return CreatePaymentResponse(paymentIntent.clientSecret)
    }
}