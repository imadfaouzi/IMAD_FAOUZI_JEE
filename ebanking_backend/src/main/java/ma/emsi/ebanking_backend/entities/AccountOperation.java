package ma.emsi.ebanking_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.ebanking_backend.enums.OperationType;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private BankAccount bankAccount;
    private String description;
}
