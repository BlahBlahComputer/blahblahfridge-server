package sogang.capstone.blahblahfridge.dto;

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
public class NaverUserDTO {

    private String resultcode;
    private String message;
    private NaverProfileDTO response;

}
