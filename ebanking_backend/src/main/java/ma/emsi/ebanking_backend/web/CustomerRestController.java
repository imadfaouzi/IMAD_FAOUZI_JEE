package ma.emsi.ebanking_backend.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.ebanking_backend.dtos.CustomerDTO;
import ma.emsi.ebanking_backend.entities.Customer;
import ma.emsi.ebanking_backend.exceptions.CustumerNotFoundException;
import ma.emsi.ebanking_backend.services.BankAcountService;
import ma.emsi.ebanking_backend.services.BankAcountServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
   private BankAcountService bankAcountService;

   @GetMapping("/customers")
   public List<CustomerDTO> customers(){
       return  bankAcountService.listCustomers();
   }

   @GetMapping("/customers/{id}")
    public  CustomerDTO customer(@PathVariable(name = "id") Long customerId)
           throws CustumerNotFoundException
   {
       return  bankAcountService.getCustomer(customerId);
   }

   @PostMapping("/customers")
   public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return   bankAcountService.saveCustomer(customerDTO);
   }

   @PutMapping("/customers/{id}")
    public  CustomerDTO updateCustomer(@PathVariable(name = "id") Long customerId,
                                       @RequestBody CustomerDTO customerDTO
                                       ){

      customerDTO.setId(customerId);
      return bankAcountService.updateCustomer(customerDTO);
   }

   @DeleteMapping("/customers/{id}")
   public void deleteCustomer(@PathVariable(name = "id") Long customerId){
         bankAcountService.deleteCustomer(customerId);
   }
 }
