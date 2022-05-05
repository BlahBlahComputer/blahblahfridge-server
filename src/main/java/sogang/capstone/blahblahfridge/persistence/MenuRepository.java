package sogang.capstone.blahblahfridge.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sogang.capstone.blahblahfridge.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    public Optional<Menu> findByName(String name);
}
