package sogang.capstone.blahblahfridge.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;

public interface MenuIngredientRepository extends JpaRepository<MenuIngredient, Long> {

    public List<MenuIngredient> findAllByMenuId(Long id);

    @Query(value="SELECT menu_id, COUNT(menu_id) AS cnt FROM menu_ingredient "
        + "WHERE ingredient_id IN (:ids) GROUP BY menu_id ORDER BY cnt DESC", nativeQuery = true)
    public List<List<Long>> findAllMenuAndCountByIngredientIdIn(@Param("ids") List<Long> ingredientIdList);

}
