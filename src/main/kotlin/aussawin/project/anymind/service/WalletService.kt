package aussawin.project.anymind.service

import aussawin.project.anymind.exception.BusinessException
import aussawin.project.anymind.model.repository.TruncatedWalletHistory
import aussawin.project.anymind.model.repository.WalletHistory
import aussawin.project.anymind.model.request.GetByDateRequest
import aussawin.project.anymind.model.response.Transaction
import aussawin.project.anymind.repository.HourlyTransactionHistoryRepository
import aussawin.project.anymind.repository.WalletRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
@Slf4j
class WalletService(private val walletRepository: WalletRepository, private val hourlyTransactionHistoryRepository: HourlyTransactionHistoryRepository) {

    fun save(transaction: Transaction): Transaction {
        // Truncated hourly
        val startDatetime: ZonedDateTime = transaction.datetime.truncatedTo(ChronoUnit.HOURS).plusHours(1)
        val transactionsHistory = hourlyTransactionHistoryRepository.findAllByTransactionDatetimeIsAfter(Timestamp.from(startDatetime.minusHours(1).toInstant()))
        if (transactionsHistory.isEmpty()) {
            println("History is empty!, insert new record with $startDatetime")
            // Find the latest record to get the latest amount!
            val latestAmount = hourlyTransactionHistoryRepository.findFirstByOrderByTransactionDatetimeDesc()
            var newAmount = transaction.amount
            latestAmount?.let { newAmount+=latestAmount.amount }
            hourlyTransactionHistoryRepository.save(TruncatedWalletHistory(Timestamp.from(startDatetime.toInstant()), newAmount))
        } else {
            println("History is not empty!, update the record where transaction_datetime is after $startDatetime")
            // Check if the $startDateTime exists in the database or not
            val trxn = hourlyTransactionHistoryRepository.findFirstByTransactionDatetimeIs(Timestamp.from(startDatetime.toInstant()))
            if (trxn == null) {
                // To insert the record with the amount of the latest record where transaction_datetime is after
                val latestTransaction = hourlyTransactionHistoryRepository.findFirstByTransactionDatetimeBeforeOrderByTransactionDatetimeDesc(Timestamp.from(startDatetime.toInstant()))
                val newAmount: Double = latestTransaction?.amount ?: transaction.amount
                hourlyTransactionHistoryRepository.save(TruncatedWalletHistory(Timestamp.from(startDatetime.toInstant()), newAmount))
            }
            transactionsHistory.forEach { i ->
                val newAmount = i.amount + transaction.amount
                hourlyTransactionHistoryRepository.updateAmountByTransactionDateTime(newAmount, i.transactionDatetime)
            }
        }
        walletRepository.save(WalletHistory(transaction))
        return transaction
    }

    fun getByDate(requestBody: GetByDateRequest): List<Transaction> {
        // Validate request
        if (!requestBody.validate()) {
            throw BusinessException("Request validation failed!")
        }

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
        // Mapped the transactions into the map
        val map = mutableMapOf<ZonedDateTime, Double>()
        allTransactions.associateByTo(map, {ZonedDateTime.ofInstant(it.transactionDatetime.toInstant(), ZoneId.of("UTC"))}, {it.amount})
        var initialAmount: Double =
            if (map.containsKey(startZonedDatetimeTruncated)) {
                map.getValue(startZonedDatetimeTruncated)
            } else {
                val latestTransaction = hourlyTransactionHistoryRepository.findFirstByTransactionDatetimeBeforeOrderByTransactionDatetimeDesc(Timestamp.from(startZonedDatetimeTruncated.toInstant()))
                latestTransaction?.amount ?: 0.00
            }
        val result: MutableList<Transaction> = mutableListOf()
        var zdt = startZonedDatetimeTruncated
        while (zdt.isBefore(endZonedDatetimeTruncated)) {
            println("Calculated the date: $zdt")
            if (map.containsKey(zdt)) {
                initialAmount = map.getValue(zdt)
            }
            result.add(Transaction(zdt, initialAmount))
            zdt = zdt.plusHours(1)
        }

        return result.stream().toList()
    }

}