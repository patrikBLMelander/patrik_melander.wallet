package org.wallet.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.wallet.enteties.db.Account;
import org.wallet.enteties.requests.CreateAccountRequest;
import org.wallet.enteties.requests.FundsOperationRequest;
import org.wallet.enteties.requests.TransactionRequest;
import org.wallet.repositories.AccountRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldGetBalance_AccountExists_ReturnsBalance() {

        String accountId = "testAccountId";
        Account mockAccount = new Account(accountId, 100.0);
        doReturn(Optional.of(mockAccount)).when(accountRepository).findAccountByAccountId(accountId);

        ResponseEntity<String> result = walletService.getBalance(accountId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Current balance: 100.0", result.getBody());
    }

    @Test
    void getBalance_AccountNotFound_ReturnsNotFound() {
        String accountId = "nonexistent";
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId(anyString());

        ResponseEntity<String> result = walletService.getBalance(accountId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Account Id nonexistent not found", result.getBody());
    }

    @Test
    public void makeTransfer_ValidTransfer_ReturnsSuccessMessage() {
        TransactionRequest transactionRequest = new TransactionRequest("senderId", "receiverId", 50.0);

        Account mockSender = new Account("senderId", 50.0);
        doReturn(Optional.of(mockSender)).when(accountRepository).findAccountByAccountId("senderId");

        Account mockReceiver =  new Account("receiverId", 0);
        doReturn(Optional.of(mockReceiver)).when(accountRepository).findAccountByAccountId("receiverId");

        ResponseEntity<String> result = walletService.makeTransfer(transactionRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("senderId successfully sent 50.0 to receiverId.", result.getBody());
    }

    @Test
    void listTransactions_AccountFound_ReturnsTransactionList() {
        String accountId = "testAccountId";
        Account mockAccount = new Account(accountId, 100.0);
        mockAccount.deposit("deposit", 50.0);
        mockAccount.withdraw("withdraw", 20.0);
        doReturn(Optional.of(mockAccount)).when(accountRepository).findAccountByAccountId(accountId);

        ResponseEntity<List<String>> result = walletService.listTransactions(accountId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, Objects.requireNonNull(result.getBody()).size());
    }

    @Test
    void listTransactions_AccountNotFound_ReturnsNotFound() {
        String accountId = "nonexistent";
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId(anyString());

        ResponseEntity<List<String>> result = walletService.listTransactions(accountId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(List.of("Account Id nonexistent not found"), result.getBody());
    }

    @Test
    void createAccount_NewAccount_ReturnsSuccessMessage() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("newAccountId", 50.0);
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId("newAccountId");

        ResponseEntity<String> result = walletService.createAccount(createAccountRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Account with Id: newAccountId created successfully.", result.getBody());
    }

    @Test
    void createAccount_AccountAlreadyExists_ReturnsBadRequest() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("existingAccountId", 50.0);
        doReturn(Optional.of(new Account("existingAccountId", 100.0))).when(accountRepository).findAccountByAccountId("existingAccountId");

        ResponseEntity<String> result = walletService.createAccount(createAccountRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Account with id existingAccountId already exists", result.getBody());
    }

    @Test
    void depositFounds_AccountFound_ReturnsSuccessMessage() {
        FundsOperationRequest fundsOperationRequest = new FundsOperationRequest("testAccountId", 50.0);
        Account mockAccount = new Account("testAccountId", 100.0);
        doReturn(Optional.of(mockAccount)).when(accountRepository).findAccountByAccountId("testAccountId");

        ResponseEntity<String> result = walletService.depositFounds(fundsOperationRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Successfully deposit 50.0", result.getBody());
    }

    @Test
    void withdrawFounds_ValidWithdrawal_ReturnsSuccessMessage() {
        FundsOperationRequest fundsOperationRequest = new FundsOperationRequest("testAccountId", 30.0);
        Account mockAccount = new Account("testAccountId", 50.0);
        doReturn(Optional.of(mockAccount)).when(accountRepository).findAccountByAccountId("testAccountId");

        ResponseEntity<String> result = walletService.withdrawFounds(fundsOperationRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Successfully withdraw 30.0", result.getBody());
    }

    @Test
    void depositFounds_AccountNotFound_ReturnsNotFound() {
        FundsOperationRequest fundsOperationRequest = new FundsOperationRequest("nonexistent", 50.0);
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId("nonexistent");

        ResponseEntity<String> result = walletService.depositFounds(fundsOperationRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Account Id nonexistent not found", result.getBody());
    }

    @Test
    void withdrawFounds_AccountNotFound_ReturnsNotFound() {
        FundsOperationRequest fundsOperationRequest = new FundsOperationRequest("nonexistent", 30.0);
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId("nonexistent");

        ResponseEntity<String> result = walletService.withdrawFounds(fundsOperationRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Account Id nonexistent not found", result.getBody());
    }

    @Test
    void makeTransfer_InvalidAccounts_ReturnsNotFound() {
        TransactionRequest transactionRequest = new TransactionRequest("senderId", "receiverId", 50.0);
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId("senderId");
        doReturn(Optional.empty()).when(accountRepository).findAccountByAccountId("receiverId");

        ResponseEntity<String> result = walletService.makeTransfer(transactionRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid account(s) involved in the transaction", result.getBody());
    }

    @Test
    void makeTransfer_InsufficientFunds_ReturnsBadRequest() {
        TransactionRequest transactionRequest = new TransactionRequest("senderId", "receiverId", 50.0);
        Account mockSender = new Account("senderId", 30.0);
        Account mockReceiver = new Account("receiverId", 0.0);
        doReturn(Optional.of(mockSender)).when(accountRepository).findAccountByAccountId("senderId");
        doReturn(Optional.of(mockReceiver)).when(accountRepository).findAccountByAccountId("receiverId");

        ResponseEntity<String> result = walletService.makeTransfer(transactionRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Insufficient funds for the transfer.", result.getBody());
    }

    @Test
    void withdrawFounds_InsufficientFunds_ReturnsBadRequest() {
        FundsOperationRequest fundsOperationRequest = new FundsOperationRequest("accountId", 30.1);
        Account account = new Account("accountId", 30.0);
        doReturn(Optional.of(account)).when(accountRepository).findAccountByAccountId("accountId");

        ResponseEntity<String> result = walletService.withdrawFounds(fundsOperationRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Insufficient funds for the transfer.", result.getBody());
    }
}