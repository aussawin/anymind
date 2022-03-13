package aussawin.project.anymind.model.response

import aussawin.project.anymind.model.repository.TruncatedWalletHistory
import aussawin.project.anymind.model.serializer.KZonedDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

@Serializable
data class Transaction(@Serializable(with = KZonedDateTimeSerializer::class) var datetime: ZonedDateTime, var amount: Double) {
    constructor(truncatedWalletHistory: TruncatedWalletHistory) : this(ZonedDateTime.ofInstant(truncatedWalletHistory.transactionDatetime.toInstant(), ZoneId.of("UTC")), truncatedWalletHistory.amount)
}