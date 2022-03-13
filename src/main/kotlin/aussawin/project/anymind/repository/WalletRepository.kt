package aussawin.project.anymind.repository

import aussawin.project.anymind.model.repository.WalletHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface WalletRepository: JpaRepository<WalletHistory, Int> {}