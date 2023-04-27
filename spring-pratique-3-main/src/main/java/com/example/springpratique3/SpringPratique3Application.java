package com.example.springpratique3;

import com.example.springpratique3.entities.Patient;
import com.example.springpratique3.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class SpringPratique3Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringPratique3Application.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository){
        return args -> {
            patientRepository.save(
                    new Patient(null,"Hassan",new Date(),false,122)
            );
            patientRepository.save(
                    new Patient(null,"Marwane",new Date(),true,123)
            );
            patientRepository.save(
                    new Patient(null,"Ftahi",new Date(),true,238)
            );

            patientRepository.findAll().forEach(p->{
                System.out.println(p.getNom());
            });

        };
    }
}
