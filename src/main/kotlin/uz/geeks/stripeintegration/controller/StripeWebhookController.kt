package uz.geeks.stripeintegration.controller

import org.springframework.web.bind.annotation.*
import uz.geeks.stripeintegration.operator.StripeWebhookOperator

@RestController
@RequestMapping("/api/merchant/payment/pull/stripe")
class StripeWebhookController {

    @PostMapping("/webhooks")
    @ResponseBody
    fun handleStripeEvents(
        @RequestHeader("Stripe-Signature") stripeSignature: String,
        @RequestBody requestBody: String
    ): String {
        return StripeWebhookOperator().handleCallback(stripeSignature, requestBody)
    }
}