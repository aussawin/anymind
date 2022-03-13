package aussawin.project.anymind.model.request

import lombok.NonNull
import java.time.ZonedDateTime

data class GetByDateRequest(@NonNull var startDatetime: ZonedDateTime, @NonNull var endDatetime: ZonedDateTime) {
    fun validate(): Boolean {
        return !startDatetime.isAfter(endDatetime)
    }
}