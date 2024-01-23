package uz.geeks.stripeintegration.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "stripe_entity")
class StripeEntity() {

    @Id
    @JsonProperty("id")
    var id: String? = null

    @JsonProperty("agent_id")
    var agentId: Int = 0

    @JsonProperty("user_id")
    var userId: Int = 0

    @JsonProperty("transaction_created_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    var transactionCreatedTime: String? = null

    @JsonProperty("amount")
    var amount: BigDecimal = BigDecimal.ZERO

    @JsonProperty("pan")
    var pan: String? = null

    @JsonProperty("expire_date")
    var expireDate: String? = null

    @JsonProperty("cvv")
    var cvv: String? = null

    @JsonProperty("state")
    var state: String? = null

    @JsonProperty("currency")
    var currency: Int = 0

    @JsonProperty("payment_type")
    var paymentType: String? = null

    @JsonProperty("base_term_url")
    var baseTermUrl: String? = null

    val isNew: Boolean get() = state == Status.NEW.name
    val isWaiting: Boolean get() = state == Status.WAITING.name
    val isCompleted: Boolean get() = state == Status.COMPLETED.name
    val isCaptured: Boolean get() = state == Status.CAPTURED.name
    val isApproved: Boolean get() = state == Status.APPROVED.name
    val isFinished: Boolean get() = state == Status.FINISHED.name
    val isDeclined: Boolean get() = state == Status.DECLINED.name
    val isRefunded: Boolean get() = state == Status.REFUNDED.name
    val isFinishFailed: Boolean get() = state == Status.FINISHFAILED.name

    val isPayment: Boolean get() = state == "payment"
    val isCapture: Boolean get() = state == "capture"
    val isRefund: Boolean get() = state == "refund"

    val isExpired: Boolean
        get() {
            val transactionTime =
                if (transactionCreatedTime?.length == 25) "${transactionCreatedTime}0" else transactionCreatedTime

            val createdTime = transactionTime?.let {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
                val parsedTime = LocalDateTime.parse(it, formatter)
                parsedTime.atZone(ZoneId.of("GMT+5")).toInstant().toEpochMilli()
            } ?: System.currentTimeMillis()

            return (System.currentTimeMillis() - createdTime) > 10 * 60 * 1_000
        }

    @get:JsonProperty("state_name")
    val stateName: String
        get() {
            return when (state) {
                Status.NEW.name -> Status.NEW.value
                Status.WAITING.name -> Status.WAITING.value
                Status.COMPLETED.name -> Status.COMPLETED.value
                Status.CAPTURED.name -> Status.CAPTURED.value
                Status.APPROVED.name -> Status.APPROVED.value
                Status.FINISHED.name -> Status.FINISHED.value
                Status.DECLINED.name -> Status.DECLINED.value
                Status.REFUNDED.name -> Status.REFUNDED.value
                Status.FINISHFAILED.name -> Status.FINISHFAILED.value
                else -> {
                    "Неизвестная"
                }
            }
        }

    @get:JsonProperty("status_color")
    val statusColor: String
        get() {
            return when (state) {
                Status.NEW.name -> Status.NEW.color
                Status.WAITING.name -> Status.WAITING.color
                Status.COMPLETED.name -> Status.COMPLETED.color
                Status.CAPTURED.name -> Status.CAPTURED.color
                Status.APPROVED.name -> Status.APPROVED.color
                Status.FINISHED.name -> Status.FINISHED.color
                Status.DECLINED.name -> Status.DECLINED.color
                Status.REFUNDED.name -> Status.REFUNDED.color
                Status.FINISHFAILED.name -> Status.FINISHFAILED.color
                else -> {
                    "red"
                }
            }
        }

    enum class Status(var color: String, name: String, var value: String) {
        NEW("blue", "NEW", "B создании"),
        WAITING("blue", "WAITING", "В ожидании"),
        COMPLETED("blue", "COMPLETED", "В ожидании"),
        CAPTURED("blue", "CAPTURED", "В ожидании"),
        APPROVED("blue", "APPROVED", "В ожидании"),
        FINISHED("green", "FINISHED", "Успешно оплачен"),
        DECLINED("red", "DECLINED", "Оплата не прошла"),
        REFUNDED("green", "REFUNDED", "Возвращено"),
        FINISHFAILED("blue", "FINISHFAILED", "В ожидании")
    }

}