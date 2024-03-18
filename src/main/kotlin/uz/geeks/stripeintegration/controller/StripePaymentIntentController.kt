package uz.geeks.stripeintegration.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uz.geeks.stripeintegration.dto.Transaction
import uz.geeks.stripeintegration.operator.StripePaymentIntentOperator

@RestController
@RequestMapping("/api/merchant/payment/pull/stripe")
class StripePaymentIntentController {

    @PostMapping("/start")
    fun createPaymentIntent(
        @RequestBody transaction: Transaction
    ): Any {

      return StripePaymentIntentOperator().createPaymentIntent(transaction)

    }
}