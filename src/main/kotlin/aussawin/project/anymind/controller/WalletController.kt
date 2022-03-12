package aussawin.project.anymind.controller

import aussawin.project.anymind.model.GetByDateRequest
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

    @GetMapping("/getTransactionByDate")
    fun getAll(@RequestBody requestBody: GetByDateRequest): List<Transaction> {
        return walletService.getByDate(requestBody)
    }

    @PostMapping("/save")
    fun save(@RequestBody transaction: Transaction): String {
        return try {
            walletService.save(transaction)
            "Success"
        } catch (ex: Exception) {
            println(ex.printStackTrace())
            "Failed"
        }
    }

}

