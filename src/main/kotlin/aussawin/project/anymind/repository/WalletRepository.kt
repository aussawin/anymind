package aussawin.project.anymind.repository

import aussawin.project.anymind.model.WalletHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository: JpaRepository<WalletHistory, Int>