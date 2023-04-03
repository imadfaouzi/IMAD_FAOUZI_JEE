package ma.emsi.hospital.repositories;

import ma.emsi.hospital.entities.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    public Medecin findMedecinById(Long id);
}
