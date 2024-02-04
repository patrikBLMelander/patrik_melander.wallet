package org.wallet.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.enteties.requests.CreateAccountRequest;
import org.wallet.enteties.requests.FundsOperationRequest;
import org.wallet.enteties.requests.TransactionRequest;
import org.wallet.services.WalletService;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<String> getBalance(@PathVariable String accountId) {
        return walletService.getBalance(accountId);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositFounds(@RequestBody FundsOperationRequest fundsOperationRequest){
        return walletService.depositFounds(fundsOperationRequest);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFounds(@RequestBody FundsOperationRequest fundsOperationRequest){
        return walletService.withdrawFounds(fundsOperationRequest);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransactionRequest transactionRequest){
        return walletService.makeTransfer(transactionRequest);
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<String>> listTransactions(@PathVariable String accountId) {
        return walletService.listTransactions(accountId);
    }

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return walletService.createAccount(createAccountRequest);
    }
}
