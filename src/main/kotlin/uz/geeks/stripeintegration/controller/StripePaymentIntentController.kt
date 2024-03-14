package uz.geeks.stripeintegration.controller

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uz.geeks.stripeintegration.dto.CreatePaymentResponse
import uz.geeks.stripeintegration.dto.Transaction
import uz.geeks.stripeintegration.utils.CredentialsUtil

@RestController
class StripePaymentIntentController {

    val stripeApiKey: String? get() = CredentialsUtil().getStripeApiKey()

    @PostMapping("/create-intent")
    fun createPaymentIntent(
        @RequestBody transaction: Transaction
    ): CreatePaymentResponse {

        Stripe.apiKey = stripeApiKey

        transaction.amount = 12
        transaction.id = 98472

        return try {

            val sum = transaction.amount * 100

            val params = PaymentIntentCreateParams.builder()
                .setAmount(sum)
                .setCurrency("aed")
                .addAllPaymentMethodType(
                    listOf(
                        "card"
                    )
                )
                .putMetadata("transaction_id", transaction.id.toString())
                .build()

            val paymentIntent = PaymentIntent.create(params)

            val createPaymentResponse = CreatePaymentResponse(paymentIntent.clientSecret)

            println("client secret " + createPaymentResponse.clientSecret)

            CreatePaymentResponse(createPaymentResponse.clientSecret)

        } catch (e: Exception) {
            e.printStackTrace()
            CreatePaymentResponse("")
        }

    }
}