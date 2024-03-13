package uz.geeks.stripeintegration.utils

import java.io.File
import org.json.JSONObject

class CredentialsUtil {

    fun getStripeApiKey(): String {
        val json = getStripeAcquiringData()
        return json.getString("api_key")
    }

    fun getStripeWebhookSecret(): String {
        val json = getStripeAcquiringData()
        return json.getString("webhook_secret")
    }

    fun getStripePublicKey(): String {
        val json = getStripeAcquiringData()
        return json.getString("public_key")
    }

    private fun getStripeAcquiringData() = readConfigJson().getJSONObject("stripe_acquiring")

    private fun readConfigJson(): JSONObject {
        var jsonStr = ""
        File(configFilePath).forEachLine { jsonStr += it + "\n" }
        val json = JSONObject(jsonStr)
        return json
    }

    val configFilePath: String
        get() ="/home/khusniddin/IdeaProjects/Stripe/StripeIntegration/config/configs.json"
}