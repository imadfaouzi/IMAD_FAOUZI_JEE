package ma.emsi.ebanking_backend.services;

import ma.emsi.ebanking_backend.dtos.*;
import ma.emsi.ebanking_backend.entities.BankAccount;
import ma.emsi.ebanking_backend.entities.CurrentAccount;
import ma.emsi.ebanking_backend.entities.Customer;
import ma.emsi.ebanking_backend.entities.SavingAccount;
import ma.emsi.ebanking_backend.exceptions.BalanceNotSufficientException;
import ma.emsi.ebanking_backend.exceptions.BankAccountNotFoundException;
import ma.emsi.ebanking_backend.exceptions.CustumerNotFoundException;

import java.util.List;

public interface BankAcountService
{
    public CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalce, double overDraft, Long customerId) throws CustumerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalce, double interestRate, Long customerId) throws CustumerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BalanceNotSufficientException;
    void transfer(String accountIdSource, String accountIdDestination,double amount);


    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long id) throws  CustumerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId) throws CustumerNotFoundException;

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size);
}
