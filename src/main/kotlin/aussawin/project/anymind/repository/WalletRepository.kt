package aussawin.project.anymind.repository

import aussawin.project.anymind.model.WalletHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Repository
@Transactional
interface WalletRepository: JpaRepository<WalletHistory, Int> {
    fun findAllByTransactionDatetimeBetweenOrderByTransactionDatetime(start: Timestamp, end: Timestamp): List<WalletHistory>
}