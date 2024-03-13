package uz.geeks.stripeintegration.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import uz.geeks.stripeintegration.form.CheckoutForm
import uz.geeks.stripeintegration.utils.CredentialsUtils

@Controller
@RequestMapping("/api/merchant/payment/pull/stripe")
class StripeFrontController {

    val stripePublicKey: String? get() = CredentialsUtils().getStripePublicKey()

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
}