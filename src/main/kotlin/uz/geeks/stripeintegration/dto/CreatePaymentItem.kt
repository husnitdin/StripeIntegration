package uz.geeks.stripeintegration.dto

import com.google.gson.annotations.SerializedName

data class CreatePaymentItem (
    @SerializedName("id")
    var id: String? = null
)