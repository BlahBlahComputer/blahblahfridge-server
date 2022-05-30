package sogang.capstone.blahblahfridge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfileDTO {

    @JsonProperty("id")
    private String authenticationCode;

    @JsonProperty("name")
    private String nickname;

    @JsonProperty("profile_image")
    private String imageUrl;
}
