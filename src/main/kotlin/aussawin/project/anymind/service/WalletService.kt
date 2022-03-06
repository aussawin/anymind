package aussawin.project.anymind.service

import aussawin.project.anymind.model.Transaction
import aussawin.project.anymind.model.WalletHistory
import aussawin.project.anymind.repository.WalletRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class WalletService(private val walletRepository: WalletRepository) {

    fun getAll(): List<Transaction> {
        val allTransactions: List<WalletHistory> = walletRepository.findAll()
        val transactionList: List<Transaction> = allTransactions.stream().map { i ->
            Transaction(
                amount = i.amount,
                datetime = ZonedDateTime.ofInstant(
                    i.createdDatetime.toInstant(),
                    ZoneId.of("UTC")
                )
            )
        }.toList()
        return transactionList
    }

    fun saveAll(transactions: List<Transaction>): List<Transaction> {
        val walletHistoryList: List<WalletHistory> = transactions.stream().map { i ->
            WalletHistory(
                id = UUID.randomUUID().toString(),
                amount = i.amount,
                createdDatetime = Timestamp.from(i.datetime.toInstant())
            )
        }.toList()
        walletRepository.saveAll(walletHistoryList)
        return transactions
    }
}