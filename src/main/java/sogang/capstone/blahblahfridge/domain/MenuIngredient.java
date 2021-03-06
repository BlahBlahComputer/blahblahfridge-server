package sogang.capstone.blahblahfridge.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "MenuIngredient")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class MenuIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Builder
    public MenuIngredient(
        Long id,
        Menu menu,
        Ingredient ingredient) {
        if (menu == null) {
            throw new RuntimeException("메뉴는 필수값입니다.");
        }
        if (ingredient == null) {
            throw new RuntimeException("재료는 필수값입니다.");
        }
        this.id = id;
        this.menu = menu;
        this.ingredient = ingredient;
    }

}