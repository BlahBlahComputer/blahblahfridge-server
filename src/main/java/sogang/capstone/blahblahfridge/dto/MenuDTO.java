package sogang.capstone.blahblahfridge.dto;

import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Menu;

@Getter
@ToString
public class MenuDTO {

    private String name;
    private String image;
    private Integer time;
    private String recipe;

    public MenuDTO(Menu menu) {
        this.name = menu.getName();
        this.image = menu.getImage();
        this.time = menu.getTime();
        this.recipe = menu.getRecipe();
    }
}
