package sogang.capstone.blahblahfridge.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Menu;

@Getter
@ToString
@EqualsAndHashCode
public class MenuDTO {

    private Long id;
    private String image;
    private String name;
    private String recipe;
    private Integer time;
    private String category;

    public MenuDTO(Menu menu) {
        this.id = menu.getId();
        this.image = menu.getImage();
        this.name = menu.getName();
        this.recipe = menu.getRecipe();
        this.time = menu.getTime();
        this.category = menu.getCategory().getName();
    }
}
