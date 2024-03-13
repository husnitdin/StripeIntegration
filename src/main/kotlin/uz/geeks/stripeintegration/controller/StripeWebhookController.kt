package uz.geeks.stripeintegration.controller

import com.fasterxml.jackson.databind.json.JsonMapper
import com.stripe.Stripe
import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Charge
import com.stripe.model.Event
import com.stripe.model.EventDataObjectDeserializer
import com.stripe.model.StripeObject
import com.stripe.net.ApiResource
import com.stripe.net.Webhook
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import uz.geeks.stripeintegration.CredentialsUtils

@RestController
@RequestMapping("/uz/geeks/start")
class StripeWebhookController {

    val endpointSecret: String? get() = CredentialsUtils().getStripeWebhookSecret()

    @PostMapping("/webhooks")
    @ResponseBody
    fun handleStripeEvents(
        @RequestHeader("Stripe-Signature") stripeSignature: String,
        @RequestBody requestBody: String
    ): String {

        val httpStatus = HttpStatus.OK
        var event: Event? = ApiResource.GSON.fromJson(requestBody, Event::class.java)

        if (endpointSecret != null) {
            try {
                event = Webhook.constructEvent(
                    requestBody, stripeSignature, endpointSecret
                )
            } catch (e: SignatureVerificationException) {
                println("⚠️  Webhook error while validating signature. e => $e")
                httpStatus.is4xxClientError
                return ""
            }
        }

        // Deserialize the nested object inside the event
        val dataObjectDeserializer: EventDataObjectDeserializer = event!!.dataObjectDeserializer
        var stripeObject: StripeObject? = null

        if (dataObjectDeserializer.getObject().isPresent) {
            stripeObject = dataObjectDeserializer.getObject().get()
        } else {
            println("StripeObject Deserialization failed, event type: ${event.type}")
        }

        println(JsonMapper().writeValueAsString("event api version ${event.apiVersion}"))
        println(JsonMapper().writeValueAsString("stripe api version ${Stripe.API_VERSION}"))

        // Handle the event
        when (event.type) {

            ("charge.succeeded") -> {

                if (stripeObject is Charge) {
                    val expMonth = stripeObject.paymentMethodDetails.card.expMonth
                    val expYear = stripeObject.paymentMethodDetails.card.expYear
                    val pan = stripeObject.paymentMethodDetails.card.last4

                    println(expMonth)
                    println(expYear)
                    println(pan)
                } else {
                    println("inside else CHARGE_SUCCEED, event type: \n ${event.type}, \nstripe class: \n${stripeObject!!::class.simpleName}")
                }
            }
            ("charge.failed") -> {
                println("In Webhook StripeWebhookType.CHARGE_FAILED: \n$stripeObject")
            }
            else -> {
                println("inside else of webhook, event type: \n${event.type}, stripe class: \n${stripeObject!!::class.simpleName}")
            }
        }
        httpStatus.is2xxSuccessful
        return ""
    }
}