package aussawin.project.anymind.model

import java.time.ZonedDateTime

data class GetByDateRequest(var startDatetime: ZonedDateTime, var endDatetime: ZonedDateTime)