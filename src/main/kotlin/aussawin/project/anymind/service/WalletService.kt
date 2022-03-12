package aussawin.project.anymind.service

import aussawin.project.anymind.model.GetByDateRequest
import aussawin.project.anymind.model.Transaction
import aussawin.project.anymind.model.repository.TruncatedWalletHistory
import aussawin.project.anymind.model.repository.WalletHistory
import aussawin.project.anymind.repository.HourlyTransactionHistoryRepository
import aussawin.project.anymind.repository.WalletRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
@Slf4j
class WalletService(private val walletRepository: WalletRepository, private val hourlyTransactionHistoryRepository: HourlyTransactionHistoryRepository) {

    fun save(transaction: Transaction): Transaction {
        // Truncated hourly
        val startDatetime: ZonedDateTime = transaction.datetime.truncatedTo(ChronoUnit.HOURS).plusHours(1)
        val history = hourlyTransactionHistoryRepository.findAllByTransactionDatetimeIsAfter(Timestamp.from(startDatetime.minusHours(1).toInstant()))
        if (history.isEmpty()) {
            println("History is empty!")
            val latestAmount = hourlyTransactionHistoryRepository.findFirstByOrderByTransactionDatetimeDesc()
            var newAmount = transaction.amount
            latestAmount?.let { newAmount+=latestAmount.amount }
            hourlyTransactionHistoryRepository.save(TruncatedWalletHistory(Timestamp.from(startDatetime.toInstant()), newAmount))
        } else {
            println("History is not empty!")
            history.forEach { i ->
                val newAmount = i.amount + transaction.amount
                hourlyTransactionHistoryRepository
                    .updateAmountByTransactionDateTime(
                        newAmount,
                        i.transactionDatetime
                    )
            }
        }
        walletRepository.save(WalletHistory(transaction))
        return transaction
    }

    fun getByDate(requestBody: GetByDateRequest): List<Transaction> {
        val startZonedDatetimeTruncated: ZonedDateTime = requestBody.startDatetime
            .truncatedTo(ChronoUnit.HOURS)
            .plusHours(1)
        val endZonedDatetimeTruncated: ZonedDateTime = requestBody.endDatetime
            .truncatedTo(ChronoUnit.HOURS)
            .plusHours(1)
        val allTransactions: List<TruncatedWalletHistory> = hourlyTransactionHistoryRepository
            .findAllByTransactionDatetimeIsBetweenOrderByTransactionDatetimeAsc(
                Timestamp.from(startZonedDatetimeTruncated.toInstant()),
                Timestamp.from(endZonedDatetimeTruncated.toInstant()))
        return allTransactions.stream().map { i -> Transaction(i) }.toList()
    }

}