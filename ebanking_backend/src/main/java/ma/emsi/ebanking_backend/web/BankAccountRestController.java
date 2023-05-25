package ma.emsi.ebanking_backend.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.ebanking_backend.dtos.AccountHistoryDTO;
import ma.emsi.ebanking_backend.dtos.AccountOperationDTO;
import ma.emsi.ebanking_backend.dtos.BankAccountDTO;
import ma.emsi.ebanking_backend.exceptions.BankAccountNotFoundException;
import ma.emsi.ebanking_backend.services.BankAcountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BankAccountRestController {

    private BankAcountService bankAcountService;


    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
          return  bankAcountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return  bankAcountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
              return bankAcountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAcountService.getAccountHistory(accountId,page,size);
    }



}
