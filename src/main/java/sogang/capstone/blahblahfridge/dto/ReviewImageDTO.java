package sogang.capstone.blahblahfridge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewImageDTO {
    String presignedURL;
    String imageURL;
}
