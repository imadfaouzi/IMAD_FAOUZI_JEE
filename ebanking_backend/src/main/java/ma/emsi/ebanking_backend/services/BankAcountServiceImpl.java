package ma.emsi.ebanking_backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.ebanking_backend.dtos.*;
import ma.emsi.ebanking_backend.entities.*;
import ma.emsi.ebanking_backend.enums.OperationType;
import ma.emsi.ebanking_backend.exceptions.BalanceNotSufficientException;
import ma.emsi.ebanking_backend.exceptions.BankAccountNotFoundException;
import ma.emsi.ebanking_backend.exceptions.CustumerNotFoundException;
import ma.emsi.ebanking_backend.mappers.BankAccountMapperImpl;
import ma.emsi.ebanking_backend.repositories.AccountOperationRepository;
import ma.emsi.ebanking_backend.repositories.BankAccountRepository;
import ma.emsi.ebanking_backend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class BankAcountServiceImpl implements BankAcountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    //Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getName());
    private BankAccountMapperImpl dtoMapper;





    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving a new Customer .");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalce, double overDraft, Long customerId) throws  CustumerNotFoundException{

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null)
            throw  new CustumerNotFoundException("Customer not found !!");

        CurrentAccount bankAccount = new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalce);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount savedOne = bankAccountRepository.save(bankAccount);
        return  dtoMapper.fromCurrentBankAccount(savedOne);
    }
    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalce, double intersetRate, Long customerId) throws  CustumerNotFoundException{

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null)
            throw  new CustumerNotFoundException("Customer not found !!");

        SavingAccount bankAccount = new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalce);
        bankAccount.setCustomer(customer);
        bankAccount.setInterestRate(intersetRate);
        SavingAccount savedOne = bankAccountRepository.save(bankAccount);
        return dtoMapper.fromSavingBankAccount(savedOne );
    }
    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(cust -> dtoMapper.fromCustomer(cust)).toList();
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException{
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("BankAccount not Found")
                );

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return  dtoMapper.fromSavingBankAccount(savingAccount);
        }
        else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return  dtoMapper.fromCurrentBankAccount(currentAccount);
        }
       // Polymorphisme
    }
    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("BankAccount not Found")
                );
        if(bankAccount.getBalance() < amount)
            throw  new BalanceNotSufficientException("Balance not sufficient !");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException{
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("BankAccount not Found")
                );

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() +  amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException{
     debit(accountIdSource, amount, "Transfer to "+accountIdDestination);
     credit(accountIdDestination, amount, "Transfer from "+accountIdSource);

    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOS =
                bankAccounts.stream().map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount) {
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return dtoMapper.fromSavingBankAccount(savingAccount);
                    } else {
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return dtoMapper.fromCurrentBankAccount(currentAccount);
                    }
                }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    

    @Override
    public  CustomerDTO getCustomer(Long id) throws  CustumerNotFoundException{
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new CustumerNotFoundException("Customer not Found !")
                );
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving a new Customer .");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }


    @Override
    public void deleteCustomer(Long customerId) throws CustumerNotFoundException{
        customerRepository.deleteById(customerId);
    }



    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> byBankAccountId = accountOperationRepository.findByBankAccountId(accountId);
        return byBankAccountId
                    .stream()
                    .map(op -> dtoMapper.fromAccountOperation(op))
                    .toList();
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent()
                .stream()
                .map(op -> dtoMapper.fromAccountOperation(op))
                .toList();
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }


}
