package org.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.enteties.db.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, long> {
}
