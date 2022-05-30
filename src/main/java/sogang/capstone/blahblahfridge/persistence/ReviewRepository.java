package sogang.capstone.blahblahfridge.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sogang.capstone.blahblahfridge.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    public List<Review> findAllByMenuId(Long id);

    public List<Review> findAllByUserId(Long id);

    public Optional<Review> findById(Long id);

    public void delete(Review review);
}
