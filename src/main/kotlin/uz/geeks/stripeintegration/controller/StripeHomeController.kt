package uz.geeks.stripeintegration.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import uz.geeks.stripeintegration.form.CheckoutForm

@Controller
@RequestMapping("/uz/geeks")
class StripeHomeController {

    @Value("\${stripe.public.key}")
    private val stripePublicKey: String? = null

    @GetMapping("/start")
    fun home(model: Model): String {
        model.addAttribute("checkoutForm", CheckoutForm())
        return "stripe_index"
    }

    @PostMapping("/pay")
    fun checkout(
        @ModelAttribute checkoutForm: CheckoutForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        return if (bindingResult.hasErrors()) {
            "stripe_index"
        } else {
            model.addAttribute("stripePublicKey", stripePublicKey)
            model.addAttribute("amount", checkoutForm.amount)
            "stripe_checkout"
        }
    }
}