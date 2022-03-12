package aussawin.project.anymind.model.repository

import aussawin.project.anymind.model.Transaction
import org.hibernate.Hibernate
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "hourly_transaction_history")
data class TruncatedWalletHistory(
    @Id
    var transactionDatetime: Timestamp = Timestamp.from(ZonedDateTime.now(ZoneId.of("UTC")).toInstant()),
    val amount: Double = 0.0
) {

    constructor(transaction: Transaction): this(Timestamp.from(transaction.datetime.toInstant()), transaction.amount)

    fun truncatedDatetime(): TruncatedWalletHistory {
        val zdt = ZonedDateTime.from(this.transactionDatetime.toInstant().atZone(ZoneId.of("UTC"))).truncatedTo(ChronoUnit.HOURS).plusHours(1)
        this.transactionDatetime = Timestamp.from(zdt.toInstant())
        return this
    }

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