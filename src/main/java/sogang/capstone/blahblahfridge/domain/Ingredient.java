package sogang.capstone.blahblahfridge.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "Ingredient")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "menuIngredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private IngredientCategory category;

    @OneToMany(mappedBy = "ingredient")
    private List<MenuIngredient> menuIngredients;

    @Builder
    public Ingredient(
        Long id,
        String name,
        IngredientCategory ingredientCategory
    ) {
        if (name == null) {
            throw new RuntimeException("이름은 필수값입니다.");
        }
        if (ingredientCategory == null) {
            throw new RuntimeException("카테고리는 필수값입니다.");
        }
        this.id = id;
        this.name = name;
        this.category = ingredientCategory;
    }
}