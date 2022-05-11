package sogang.capstone.blahblahfridge.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sogang.capstone.blahblahfridge.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    public List<Menu> findAll();

    public Optional<Menu> findByName(String name);
}
