package sogang.capstone.blahblahfridge.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;

@Getter
@ToString
@EqualsAndHashCode
public class MenuIngredientDTO {

    private String name;
    private String category;

    public MenuIngredientDTO(MenuIngredient menuIngredient) {
        this.name = menuIngredient.getIngredient().getName();
        this.category = menuIngredient.getIngredient().getCategory().getName();
    }
}
