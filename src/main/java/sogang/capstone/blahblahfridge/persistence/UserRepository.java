package sogang.capstone.blahblahfridge.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sogang.capstone.blahblahfridge.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);

}
