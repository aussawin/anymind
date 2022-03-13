package aussawin.project.anymind.model.repository

import org.hibernate.Hibernate
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * This model used for table: hourly_transaction_history
 */
@Entity
@Table(name = "hourly_transaction_history")
data class TruncatedWalletHistory(
    @Id
    var transactionDatetime: Timestamp = Timestamp.from(ZonedDateTime.now(ZoneId.of("UTC")).toInstant()),
    val amount: Double = 0.0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as TruncatedWalletHistory

        return transactionDatetime == other.transactionDatetime
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(transactionDatetime = $transactionDatetime )"
    }
}