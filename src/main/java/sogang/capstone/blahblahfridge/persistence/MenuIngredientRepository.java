package sogang.capstone.blahblahfridge.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;

public interface MenuIngredientRepository extends JpaRepository<MenuIngredient, Long> {

    public List<MenuIngredient> findAllByMenuId(Long id);

    public List<MenuIngredient> findAllByIngredientIdIn(List<Long> ingredientIdList);

}
