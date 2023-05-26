package com.emsi.patients_mvc.web;

import com.emsi.patients_mvc.entities.Patient;
import com.emsi.patients_mvc.reposotories.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class patientController {
    private PatientRepository patientRepository;
    @GetMapping(path="/user/index")
   public String patients(Model model,
                          @RequestParam(name = "page",defaultValue = "0")int page,
                          @RequestParam(name = "size",defaultValue = "5")int size,
                          @RequestParam(name = "keyword",defaultValue = "")String keyword){
        Page<Patient> pagepatients=patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
       model.addAttribute("listePatients", pagepatients.getContent());
       model.addAttribute("pages" , new int[pagepatients.getTotalPages()]);
       model.addAttribute("curentPage",page);
       model.addAttribute("keyword",keyword);
       return "patients";
   }
    @GetMapping(path="/admin/delete")
    @PreAuthorize("hasRole('ROLE ADMIN')")
   public String delete(Long id,String keyword ,int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
   }

    @GetMapping(path="/")
    public String home(){
        return "redirect:/user/index";
    }
    @GetMapping(path="/patients")
    @ResponseBody
    public List<Patient> lispatient(){
        return patientRepository.findAll();
    }
    @GetMapping(path="/admin/formPatients")
    @PreAuthorize("hasRole('ROLE ADMIN')")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }
    @PostMapping(path="/admin/save")
    @PreAuthorize("hasRole('ROLE ADMIN')")
    public String save(Model model ,@Valid Patient patient,@RequestParam(defaultValue = "") String keyword ,@RequestParam(defaultValue = "0") int page, BindingResult bindingResult  ){
       if (bindingResult.hasErrors()) return  "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword"+keyword;
    }
    @GetMapping(path="/admin/editPatient")
    @PreAuthorize("hasRole('ROLE ADMIN')")
    public String editPatient(Model model,Long id,String keyword,Integer page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "editPatient";
    }



}
