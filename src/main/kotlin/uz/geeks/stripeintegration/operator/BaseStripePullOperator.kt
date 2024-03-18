package uz.geeks.stripeintegration.operator

import uz.geeks.stripeintegration.utils.BuildConfig.BASE_URL
import uz.geeks.stripeintegration.utils.CredentialsUtil

open class BaseStripePullOperator{

    val stripeApiKey: String get() = CredentialsUtil().getStripeApiKey()
    val webhookSecret: String get() = CredentialsUtil().getStripeWebhookSecret()

    val API_URL_CHECK = "${BASE_URL}/api/merchant/payment/pull/stripe/pay/"

}