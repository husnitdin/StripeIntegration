package uz.geeks.stripeintegration.operator

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import uz.geeks.stripeintegration.dto.CreatePaymentResponse
import uz.geeks.stripeintegration.dto.Transaction

class StripePaymentIntentOperator: BaseStripePullOperator() {

    fun createPaymentIntent(
       transaction: Transaction
    ): Any {

        Stripe.apiKey = stripeApiKey

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

            val createPaymentResponse = CreatePaymentResponse(PaymentIntent.create(params).clientSecret)

            return "$API_URL_CHECK${createPaymentResponse.clientSecret}"

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}