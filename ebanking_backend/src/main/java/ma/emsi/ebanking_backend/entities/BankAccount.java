package ma.emsi.ebanking_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.ebanking_backend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAccount {

    @Id
    private  String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private  Customer customer;
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.REMOVE)
    private List<AccountOperation> accountOperations;
}
