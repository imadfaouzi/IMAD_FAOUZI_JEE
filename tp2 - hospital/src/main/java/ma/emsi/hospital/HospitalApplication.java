package ma.emsi.hospital;

import ma.emsi.hospital.entities.*;
import ma.emsi.hospital.repositories.ConsultationRepository;
import ma.emsi.hospital.repositories.MedecinRepository;
import ma.emsi.hospital.repositories.PatientRepository;
import ma.emsi.hospital.repositories.RendezVousRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(
            MedecinRepository medecinRepository,
            ConsultationRepository consultationRepository,
            RendezVousRepository rendezVousRepository,
            PatientRepository patientRepository
    ){
        return args -> {
            Stream.of("ali", "hassan", "najat")
                    .forEach(name -> {
                        Patient patient = new Patient();
                        patient.setNom(name);
                        patient.setMalade(false);
                        patient.setDateNaissance(new Date());
                        patientRepository.save(patient);
                    });
            Stream.of("wazani", "benrahou", "benzydon")
                    .forEach(name -> {
                        Medecin medecin = new Medecin();
                        medecin.setNom(name);
                        medecin.setEmail(name+"@hospital.com");
                        medecin.setSpecialite(Math.random() > 0.5 ? "Cardio":"Dentiste");
                        medecinRepository.save(medecin);
                    });

            //---------------------------------------------
            Medecin medecinById = medecinRepository.findMedecinById(1L);
            Patient patientById = patientRepository.findPatientById(2L);
            //--------------------------------------------------
            RendezVous rendezVous = new RendezVous();
            rendezVous.setStatus(StatusRDV.PENDING);
            rendezVous.setDate(new Date());
            rendezVous.setPatient(patientById);
            rendezVous.setMedecin(medecinById);
            rendezVousRepository.save(rendezVous);

            RendezVous rendezVous1 = rendezVousRepository.findRendezVousById(1L);
            Consultation consultation = new Consultation();
            consultation.setDateConsultation(new Date());
            consultation.setRendezVous(rendezVous1);
            consultation.setRapport("rapport de la consultation ...");
            consultationRepository.save(consultation);

        };
    }

}
