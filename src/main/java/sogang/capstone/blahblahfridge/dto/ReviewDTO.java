package sogang.capstone.blahblahfridge.dto;

import java.sql.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Review;

@Getter
@ToString
@EqualsAndHashCode
public class ReviewDTO {

    private String username;
    private String userimage;
    private String image;
    private String content;
    private Integer rate;
    private Timestamp created_at;

    public ReviewDTO(Review review) {
        this.username = review.getUser().getUsername();
        this.userimage = review.getUser().getImage();
        this.image = review.getImage();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.created_at = review.getCreatedAt();
    }
}
