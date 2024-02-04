package org.wallet.enteties.requests;

public record TransactionRequest(String senderId, String receiverId, double transferAmount) {
}
