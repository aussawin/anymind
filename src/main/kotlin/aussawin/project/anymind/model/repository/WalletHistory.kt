package aussawin.project.anymind.model.repository

import aussawin.project.anymind.model.Transaction
import org.apache.logging.log4j.util.Strings
import org.hibernate.Hibernate
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wallet_history")
data class WalletHistory(
    @Id
    val id: String = Strings.EMPTY,
    val amount: Double = 0.0,
    val transactionDatetime: Timestamp = Timestamp.from(ZonedDateTime.now(ZoneId.of("UTC")).toInstant())) {

    constructor(transaction: Transaction): this(UUID.randomUUID().toString(), transaction.amount, Timestamp.from(transaction.datetime.toInstant()))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as WalletHistory

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}