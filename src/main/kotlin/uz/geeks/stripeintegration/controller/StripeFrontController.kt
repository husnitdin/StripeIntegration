package uz.geeks.stripeintegration.controller

import org.springframework.http.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import uz.geeks.stripeintegration.dto.Transaction
import uz.geeks.stripeintegration.form.CheckoutForm
import uz.geeks.stripeintegration.utils.CredentialsUtil

@Controller
@RequestMapping("/api/merchant/payment/pull/stripe")
class StripeFrontController {

    val stripePublicKey: String? get() = CredentialsUtil().getStripePublicKey()

    @PostMapping("/start")
    fun start(
        @RequestBody transaction: Transaction
    ):String {

        val restTemplate = RestTemplate()
        val url = "http://localhost:8080/create-intent"

        val requestBody = mapOf(
            "amount" to transaction.amount,
            "id" to transaction.id
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(requestBody, headers)
        val responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String::class.java)

        return "http://localhost:8080/api/merchant/payment/pull/stripe/pay/${responseEntity.body}"
    }

    @GetMapping("/pay/{secret_key}")
    fun checkout(
        @PathVariable("secret_key") clientSecret: String,
        @ModelAttribute checkoutForm: CheckoutForm,
        bindingResult: BindingResult,
        model: Model
    ): String {

        return if (bindingResult.hasErrors()) {
            "stripe_index"
        } else {
            model.addAttribute("stripePublicKey", stripePublicKey)
            model.addAttribute("clientSecret", clientSecret)
            "stripe_checkout"
        }
    }

    @GetMapping("/wait")
    fun paymentResponse(
    ): Any? {
        return "stripe_waiting_page"
    }

}