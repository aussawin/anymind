package aussawin.project.anymind.repository

import aussawin.project.anymind.model.repository.TruncatedWalletHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
@Transactional
interface HourlyTransactionHistoryRepository: JpaRepository<TruncatedWalletHistory, Int> {
    fun findAllByTransactionDatetimeIsAfter(transactionDatetime: Timestamp): List<TruncatedWalletHistory>

    @Modifying
    @Query("update TruncatedWalletHistory h set h.amount = ?1 where h.transactionDatetime = ?2")
    fun updateAmountByTransactionDateTime(amount: Double, transactionDatetime: Timestamp)

    fun findFirstByOrderByTransactionDatetimeDesc(): TruncatedWalletHistory?
    fun findAllByTransactionDatetimeIsBetween(start: Timestamp, end: Timestamp): List<TruncatedWalletHistory>
}