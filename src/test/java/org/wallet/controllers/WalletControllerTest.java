package org.wallet.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wallet.enteties.requests.CreateAccountRequest;
import org.wallet.enteties.requests.FundsOperationRequest;
import org.wallet.enteties.requests.TransactionRequest;
import org.wallet.services.WalletService;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void statusCodeOk_whenCreateNewAccount_dontAlreadyExists() throws Exception {
        CreateAccountRequest requset = new CreateAccountRequest("newAccount", 20);
        mockMvc.perform(
                post("/wallet/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requset))
        ).andExpect(status().isOk());
    }

    @Test
    void statusCodeOk_whenGetBalance() throws Exception {
        String accountId = "testAccountId";
        doReturn(ResponseEntity.ok("Current balance: 100.0")).when(walletService).getBalance(accountId);

        mockMvc.perform(get("/wallet/balance/{accountId}", accountId))
                .andExpect(status().isOk());
    }

    @Test
    void statusCodeOk_whenDepositFunds() throws Exception {
        FundsOperationRequest request = new FundsOperationRequest("testAccountId", 50.0);
        doReturn(ResponseEntity.ok("Successfully deposit 50.0")).when(walletService).depositFounds(request);

        mockMvc.perform(
                post("/wallet/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void statusCodeOk_whenWithdrawFunds() throws Exception {
        FundsOperationRequest request = new FundsOperationRequest("testAccountId", 30.0);
        doReturn(ResponseEntity.ok("Successfully withdraw 30.0")).when(walletService).withdrawFounds(request);

        mockMvc.perform(
                post("/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void statusCodeOk_whenTransferFunds() throws Exception {
        TransactionRequest request = new TransactionRequest("senderId", "receiverId", 50.0);
        doReturn(ResponseEntity.ok("senderId successfully sent 50.0 to receiverId.")).when(walletService).makeTransfer(request);

        mockMvc.perform(
                post("/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }

    @Test
    void statusCodeOk_whenListTransactions() throws Exception {
        String accountId = "testAccountId";
        doReturn(ResponseEntity.ok(List.of("Transaction 1", "Transaction 2"))).when(walletService).listTransactions(accountId);

        mockMvc.perform(get("/wallet/transactions/{accountId}", accountId))
                .andExpect(status().isOk());
    }
}
