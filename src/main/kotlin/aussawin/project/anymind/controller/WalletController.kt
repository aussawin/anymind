package aussawin.project.anymind.controller

import aussawin.project.anymind.model.Transaction
import aussawin.project.anymind.service.WalletService
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
class WalletController(private val walletService: WalletService) {
    @GetMapping("/getTransaction")
    fun getAll(): List<Transaction> {
        return walletService.getAll()
    }

    @PostMapping("/save")
    fun save(@RequestBody transactions: List<Transaction>): List<Transaction> {
        return walletService.saveAll(transactions)
    }
}

