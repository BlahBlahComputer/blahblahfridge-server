package sogang.capstone.blahblahfridge.dto;

import java.sql.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.Review;

@Getter
@ToString
@EqualsAndHashCode
public class ReviewResponseDTO {

    private Long id;
    private String username;
    private String userimage;
    private String image;
    private String content;
    private Integer rate;
    private Timestamp created_at;
    private boolean deletable;

    public ReviewResponseDTO(Review review, boolean deletable) {
        this.id = review.getId();
        this.username = review.getUser().getUsername();
        this.userimage = review.getUser().getImage();
        this.image = review.getImage();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.created_at = review.getCreatedAt();
        this.deletable = deletable;
    }
}
