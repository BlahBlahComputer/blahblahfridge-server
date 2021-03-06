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
@Table(name = "MenuCategory")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "menus")
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Menu> menus;

    @Builder
    public MenuCategory(Long id, String name) {
        this.id = id;
        if (name == null) {
            throw new RuntimeException("이름은 필수값입니다.");
        }
        this.name = name;
    }
}