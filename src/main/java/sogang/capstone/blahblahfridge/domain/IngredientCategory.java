package sogang.capstone.blahblahfridge.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "IngredientCategory")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "ingredients")
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Ingredient> ingredients;

    @Builder
    public IngredientCategory(
        Long id,
        String name
    ) {
        if (name == null) {
            throw new RuntimeException("이름은 필수값입니다.");
        }
        this.id = id;
        this.name = name;
    }
}