package uz.geeks.stripeintegration

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uz.geeks.stripeintegration.entity.StripeEntity
import java.math.BigDecimal

@Repository
interface StripeRepo : JpaRepository<StripeEntity, String> {

    @Modifying
    @Query(
        value = "INSERT INTO stripe_entity (id, agent_id, user_id, transaction_created_time, amount, pan, expire_date, cvv, state, base_term_url, payment_type, currency) " +
                "VALUES (:id, :agentId, :userId, :transactionCreatedTime, :amount, :pan, :expireDate, :cvv, :state, :baseTermUrl, :paymentType, :currency)",
        nativeQuery = true
    )
    fun saveData(
        id: String? = null,
        agentId: Int? = null,
        userId: Int? = null,
        transactionCreatedTime: String? = null,
        amount: BigDecimal? = null,
        pan: String? = null,
        expireDate: String? = null,
        cvv: String? = null,
        state: String? = null,
        baseTermUrl: String? = null,
        paymentType: String? = null,
        currency: Int? = null
    ) : StripeEntity


    @Modifying
    @Query(value = "UPDATE stripe_entity SET state = :state WHERE id = :id",
        nativeQuery = true)
    fun updateStatus(state: String, id: String): Boolean

}
