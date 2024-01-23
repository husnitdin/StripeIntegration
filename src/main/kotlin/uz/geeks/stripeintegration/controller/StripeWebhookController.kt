package uz.geeks.stripeintegration.controller

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.model.EventDataObjectDeserializer
import com.stripe.model.PaymentIntent
import com.stripe.model.PaymentMethod
import com.stripe.model.StripeObject
import com.stripe.net.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class StripeWebhookController {

    @Value("\${stripe.webhook.secret}")
    val endpointSecret: String? = null

    @PostMapping("/stripe/events")
    fun handleStripeEvents(
        @RequestBody payload: String,
        @RequestHeader("Stripe-Signature") sigHeader: String
    ): String {

        val event: Event

        try {
            event = Webhook.constructEvent(
                payload, sigHeader, endpointSecret
            );
        } catch (e: SignatureVerificationException) {
            println("⚠️  Webhook error while validating signature. e => $e");
            return "";
        }

        // Deserialize the nested object inside the event
        val dataObjectDeserializer: EventDataObjectDeserializer  = event.dataObjectDeserializer
        var stripeObject: StripeObject?  = null

        if (dataObjectDeserializer.getObject().isPresent) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        // Handle the event
        when (event.type) {
            "payment_intent.succeeded" -> {
                val paymentIntent = stripeObject as? PaymentIntent
                if (paymentIntent != null) {
                    println("Payment for ${paymentIntent.amount} succeeded.")
                }
            }
            "payment_method.attached" -> {
                val paymentMethod = stripeObject as? PaymentMethod
                if (paymentMethod != null) {
                    println("Payment for payment_method.attached")
                }
            }
            else -> {
                println("Unhandled event type: ${event.type}")
            }
        }
//        response.status(200);
        return "";
    }
}

