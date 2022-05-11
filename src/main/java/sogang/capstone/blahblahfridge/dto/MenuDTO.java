package sogang.capstone.blahblahfridge.dto;

import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Menu;

@Getter
@ToString
public class MenuDTO {

    private String image;
    private String name;
    private String recipe;
    private Integer time;
    private String category;

    public MenuDTO(Menu menu) {
        this.image = menu.getImage();
        this.name = menu.getName();
        this.recipe = menu.getRecipe();
        this.time = menu.getTime();
        this.category = menu.getCategory().getName();
    }
}
