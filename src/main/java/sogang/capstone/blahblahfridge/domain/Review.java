package sogang.capstone.blahblahfridge.domain;

import javax.persistence.Column;
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
@Table(name = "Review")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Review extends AbstractTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder()
    public Review(
        Long id,
        Integer rate,
        String content,
        String image,
        User user,
        Menu menu
    ) {
        if (rate == null) {
            throw new RuntimeException("평점점은 필값입니다.");
        }
        if (content == null) {
            throw new RuntimeException("내용은 필수값입니다.");
        }
        if (user == null) {
            throw new RuntimeException("유저는 필수값입니다.");
        }
        if (menu == null) {
            throw new RuntimeException("메뉴는 필수값입니다.");
        }
        this.id = id;
        this.rate = rate;
        this.content = content;
        this.image = image;
        this.user = user;
        this.menu = menu;
    }
}