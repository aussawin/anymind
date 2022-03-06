package aussawin.project.anymind.service

import aussawin.project.anymind.model.GetByDateRequest
import aussawin.project.anymind.model.Transaction
import aussawin.project.anymind.model.WalletHistory
import aussawin.project.anymind.repository.WalletRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
@Slf4j
class WalletService(private val walletRepository: WalletRepository) {

    fun getAll(): List<Transaction> {
        val allTransactions: List<WalletHistory> = walletRepository.findAll()
        return allTransactions.stream().map { i -> Transaction(i) }.toList()
    }

    fun save(transaction: Transaction): Transaction {
        walletRepository.save(WalletHistory(transaction))
        return transaction
    }

//    fun saveAll(transactions: List<Transaction>): List<Transaction> {
//        val allTransaction: List<WalletHistory> = transactions.stream().map { i -> WalletHistory(i) }.toList()
//        walletRepository.saveAll(allTransaction)
//        return transactions
//    }

    fun getByDate(requestBody: GetByDateRequest): List<Transaction> {
        val result: List<WalletHistory> = walletRepository.findAllByTransactionDatetimeBetweenOrderByTransactionDatetime(
            Timestamp.from(requestBody.startDatetime.toInstant()),
            Timestamp.from(requestBody.endDatetime.toInstant())
        )
        return result.stream().map { i -> Transaction(i) }.toList()
    }

}