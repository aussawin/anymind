package aussawin.project.anymind.model

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class Transaction(
    @Serializable(with = KZonedDateTimeSerializer::class)
    var datetime: ZonedDateTime,
    var amount: Double)