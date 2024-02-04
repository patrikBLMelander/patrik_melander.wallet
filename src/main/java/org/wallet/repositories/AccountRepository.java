package org.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.enteties.db.Account;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findAccountByAccountId(String accountId);
}
