package aussawin.project.anymind.model

import java.sql.Timestamp
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wallet_history")
data class WalletHistory(
    @Id
    val id: String,
    val amount: Double,
    val createdDatetime: Timestamp
) {
    private constructor(): this("", 0.0, Timestamp.from(ZonedDateTime.now().toInstant()))
}