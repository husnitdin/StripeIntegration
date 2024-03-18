package uz.geeks.stripeintegration.operator

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.model.EventDataObjectDeserializer
import com.stripe.model.StripeObject
import com.stripe.net.ApiResource
import com.stripe.net.Webhook
import org.springframework.http.HttpStatus

class StripeWebhookOperator: BaseStripePullOperator() {

    fun handleCallback(
        stripeSignature: String,
        requestBody: String
    ): String {

        val httpStatus = HttpStatus.OK
        var event: Event? = ApiResource.GSON.fromJson(requestBody, Event::class.java)

        if (webhookSecret != null) {
            try {
                event = Webhook.constructEvent(
                    requestBody, stripeSignature, webhookSecret
                )
            } catch (e: SignatureVerificationException) {
                e.printStackTrace()
                httpStatus.is4xxClientError
                return ""
            }
        }

        val dataObjectDeserializer: EventDataObjectDeserializer = event!!.dataObjectDeserializer
        val stripeObject: StripeObject?

        if (dataObjectDeserializer.getObject().isPresent) {
            stripeObject = dataObjectDeserializer.getObject().get()
        } else {

            val msg = "dataObjectDeserializer.getObject() is NOT present"
            throw RuntimeException(msg)
        }

        // Handle the event
        when (event.type) {

            ("charge.succeeded") -> {
                println("charge succeeded")
            }

            ("charge.failed") -> {
                println("charge failed")
            }

            ("charge.refund.updated") -> {
                println("refund succeeded")
            }

        }

        httpStatus.is2xxSuccessful
        return ""
    }

}