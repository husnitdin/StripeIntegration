package uz.geeks.stripeintegration.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import uz.geeks.stripeintegration.dto.Transaction
import uz.geeks.stripeintegration.form.CheckoutForm
import uz.geeks.stripeintegration.utils.CredentialsUtil

@Controller
@RequestMapping("/api/merchant/payment/pull/stripe")
class StripeFrontController {

    val stripePublicKey: String? get() = CredentialsUtil().getStripePublicKey()

    @GetMapping("/pay")
    fun checkout(
        @ModelAttribute checkoutForm: CheckoutForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        return if (bindingResult.hasErrors()) {
            "stripe_index"
        } else {
            model.addAttribute("stripePublicKey", stripePublicKey)
            "stripe_checkout"
        }
    }

    @GetMapping("/wait")
    fun paymentResponse(
    ): Any? {
        return "stripe_waiting_page"
    }

}