package aussawin.project.anymind.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object KZonedDateTimeSerializer : KSerializer<ZonedDateTime> {

    private const val LONG_FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ssXXX"

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(
            value.format(
                DateTimeFormatter
                    .ofPattern(LONG_FORMAT_DATETIME)
                    .withZone(ZoneId.of("UTC"))
            ).toString()
                .replace("Z", "+00:00"))
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string)
    }
}
