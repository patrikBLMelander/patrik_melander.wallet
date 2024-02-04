package org.wallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wallet.enteties.db.Account;
import org.wallet.enteties.db.Transaction;
import org.wallet.enteties.TransactionSummary;
import org.wallet.enteties.requests.CreateAccountRequest;
import org.wallet.enteties.requests.FundsOperationRequest;
import org.wallet.enteties.requests.TransactionRequest;
import org.wallet.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final AccountRepository accountRepository;

    @Autowired
    public WalletService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<String> getBalance(final String accountId) {
        final Optional<Account> account = accountRepository.findAccountByAccountId(accountId);
        if (account.isPresent()) {
            return ResponseEntity.ok(String.format("Current balance: %s", account.get().getBalance()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Account Id %s not found", accountId));
        }
    }

    public ResponseEntity<String> makeTransfer(final TransactionRequest transactionRequest) {
        final String senderId = transactionRequest.senderId();
        final String receiverId = transactionRequest.receiverId();
        final double amount = transactionRequest.transferAmount();

        final Optional<Account> sender = accountRepository.findAccountByAccountId(senderId);
        final Optional<Account> receiver = accountRepository.findAccountByAccountId(receiverId);
        if (sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid account(s) involved in the transaction");
        }
        if (sender.get().checkBalance(amount)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient funds for the transfer.");
        }

        sender.get().withdraw(receiverId, amount);
        receiver.get().deposit(senderId, amount);

        accountRepository.save(sender.get());
        accountRepository.save(receiver.get());

        return ResponseEntity.ok(String.format("%s successfully sent %s to %s.", senderId, amount, receiverId));
    }

    public ResponseEntity<List<String>> listTransactions(final String accountId) {
        Optional<Account> account = accountRepository.findAccountByAccountId(accountId);
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(String.format("Account Id %s not found", accountId)));
        }
        List<Transaction> transactions = account.get().getTransactions();

        List<TransactionSummary> transactionSummaries = transactions.stream()
                .map(transaction -> transaction.toTransactionSummary(accountId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(transactionSummaries.stream()
                .map(summary -> String.format("%s %s %.2fkr", summary.timestamp(), summary.description(), summary.amount()))
                .collect(Collectors.toList()));
    }

    public ResponseEntity<String> createAccount(final CreateAccountRequest createAccountRequest) {
        final String accountId = createAccountRequest.accountId();
        final double amount = createAccountRequest.deposit();
        Optional<Account> account = accountRepository.findAccountByAccountId(accountId);

        if (account.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Account with id %s already exists", accountId));
        }
        Account newAccount = new Account(accountId, amount);
        accountRepository.save(newAccount);

        return ResponseEntity.ok(String.format("Account with Id: %s created successfully.", accountId));
    }

    public ResponseEntity<String> depositFounds(FundsOperationRequest fundsOperationRequest) {
        Optional<Account> account = accountRepository.findAccountByAccountId(fundsOperationRequest.accountId());
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Account Id %s not found", fundsOperationRequest.accountId()));
        }
        account.get().deposit("deposit", fundsOperationRequest.amount());
        accountRepository.save(account.get());
        return ResponseEntity.ok(String.format("Successfully deposit %s", fundsOperationRequest.amount()));
    }

    public ResponseEntity<String> withdrawFounds(FundsOperationRequest fundsOperationRequest) {
        Optional<Account> optionalAccount = accountRepository.findAccountByAccountId(fundsOperationRequest.accountId());
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Account Id %s not found", fundsOperationRequest.accountId()));
        }
        Account account = optionalAccount.get();
        final double amount = fundsOperationRequest.amount();
        if (account.checkBalance(amount)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient funds for the transfer.");
        }
        account.withdraw("withdraw", amount);
        accountRepository.save(account);
        return ResponseEntity.ok(String.format("Successfully withdraw %s", amount));
    }
}
