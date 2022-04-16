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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "Menu")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"menuIngredients", "reviews"})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private Integer time;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String recipe;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @OneToMany(mappedBy = "menu")
    private List<MenuIngredient> menuIngredients;

    @OneToMany(mappedBy = "menu")
    private List<Review> reviews;
}