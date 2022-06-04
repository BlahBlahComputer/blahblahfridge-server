package sogang.capstone.blahblahfridge.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;

@Getter
@ToString
@EqualsAndHashCode
public class MenuIngredientDTO {

    private Long id;
    private String name;
    private String category;

    public MenuIngredientDTO(MenuIngredient menuIngredient) {
        this.id = menuIngredient.getId();
        this.name = menuIngredient.getIngredient().getName();
        this.category = menuIngredient.getIngredient().getCategory().getName();
    }
}
