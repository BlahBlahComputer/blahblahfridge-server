package sogang.capstone.blahblahfridge.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sogang.capstone.blahblahfridge.domain.Ingredient;
import sogang.capstone.blahblahfridge.domain.Menu;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    public List<Ingredient> findAllByNameIn(List<String> nameList);
}
