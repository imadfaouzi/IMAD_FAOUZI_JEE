package ma.emsi.ebanking_backend.repositories;

import ma.emsi.ebanking_backend.entities.BankAccount;
import ma.emsi.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
}
