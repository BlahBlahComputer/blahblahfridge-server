package sogang.capstone.blahblahfridge.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TokenDTO {

    Long id;

    public TokenDTO(Long id) {
        this.id = id;
    }
}
