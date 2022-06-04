package sogang.capstone.blahblahfridge.dto;

import java.sql.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Review;

@Getter
@ToString
@EqualsAndHashCode
public class UserReviewDTO {

    private Long id;
    private String name;
    private String image;
    private String content;
    private Integer rate;
    private Timestamp created_at;

    public UserReviewDTO(Review review) {
        this.id = review.getId();
        this.name = review.getMenu().getName();
        this.image = review.getImage();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.created_at = review.getCreatedAt();
    }
}
