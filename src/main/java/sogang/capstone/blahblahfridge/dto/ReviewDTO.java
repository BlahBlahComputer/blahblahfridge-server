package sogang.capstone.blahblahfridge.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Review;

@Getter
@ToString
public class ReviewDTO {

    private String username;
    private String content;
    private Integer rate;
    private Timestamp created_at;

    public ReviewDTO(Review review) {
        this.username = review.getUser().getUsername();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.created_at = review.getCreatedAt();
    }
}
