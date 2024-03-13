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
class StripePaymentController {

    val stripeApiKey: String? get() = CredentialsUtil().getStripeApiKey()

    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(
        @RequestBody transaction: Transaction
    ): CreatePaymentResponse {

        Stripe.apiKey = stripeApiKey

        transaction.amount = 75
        transaction.id = 738509380280

        return try{

            val amount = transaction.amount * 100

            val params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("aed")
                .addAllPaymentMethodType(
                    listOf(
                        "card"))
                .putMetadata("transaction_id", transaction.id.toString())
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