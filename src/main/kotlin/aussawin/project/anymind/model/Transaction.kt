package aussawin.project.anymind.model

import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class Transaction(
    @Serializable(with = KZonedDateTimeSerializer::class)
    var datetime: ZonedDateTime,
    var amount: Double) {
    constructor(walletHistory: WalletHistory) : this(ZonedDateTime.ofInstant(walletHistory.transactionDatetime.toInstant(), ZoneId.of("UTC")), walletHistory.amount)
}