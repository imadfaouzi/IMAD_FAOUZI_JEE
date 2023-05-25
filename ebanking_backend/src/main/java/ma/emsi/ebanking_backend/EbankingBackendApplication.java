package ma.emsi.ebanking_backend;

import ma.emsi.ebanking_backend.dtos.CurrentBankAccountDTO;
import ma.emsi.ebanking_backend.dtos.CustomerDTO;
import ma.emsi.ebanking_backend.dtos.SavingBankAccountDTO;
import ma.emsi.ebanking_backend.entities.*;
import ma.emsi.ebanking_backend.enums.AccountStatus;
import ma.emsi.ebanking_backend.enums.OperationType;
import ma.emsi.ebanking_backend.exceptions.BalanceNotSufficientException;
import ma.emsi.ebanking_backend.exceptions.BankAccountNotFoundException;
import ma.emsi.ebanking_backend.exceptions.CustumerNotFoundException;
import ma.emsi.ebanking_backend.repositories.AccountOperationRepository;
import ma.emsi.ebanking_backend.repositories.BankAccountRepository;
import ma.emsi.ebanking_backend.repositories.CustomerRepository;
import ma.emsi.ebanking_backend.services.BankAcountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAcountService bankAcountService){
        return  args -> {

            Stream.of("Hassan", "Yassine", "Aicha")
                    .forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAcountService.saveCustomer(customer);
            });

            bankAcountService.listCustomers().forEach(customer -> {
                try{
                 bankAcountService
                         .saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                 bankAcountService
                            .saveSavingBankAccount(Math.random()*12000, 5.5, customer.getId());


                }catch (CustumerNotFoundException e){
                    e.printStackTrace();
                }catch (BankAccountNotFoundException |BalanceNotSufficientException e){
                    e.printStackTrace();
                }

                try{
                bankAcountService.bankAccountList().forEach(account -> {
                    for(int i=0; i<5; i++){
                        String accountId;
                        if(account instanceof SavingBankAccountDTO){
                            accountId = ((SavingBankAccountDTO) account).getId();
                        }else{
                            accountId = ((CurrentBankAccountDTO) account).getId();
                        }

                        bankAcountService.credit(accountId, 10000+Math.random()*120000, "Credit");
                        bankAcountService.debit(accountId, 1000+Math.random()*9000, "Debit");
                    }
                });
                }catch (CustumerNotFoundException e){
                    e.printStackTrace();
                }catch (BankAccountNotFoundException |BalanceNotSufficientException e){
                    e.printStackTrace();
                }

            });

        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return  args -> {
            Stream.of("Mouad", "driss", "Fatima").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc -> {
                for(int i = 0; i<5; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.CREDIT:OperationType.DEBIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });


        };
    }
}
