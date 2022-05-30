package sogang.capstone.blahblahfridge.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sogang.capstone.blahblahfridge.domain.User;

@Getter
@ToString
@EqualsAndHashCode
public class UserDTO {

    String username;
    String image;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.image = user.getImage();
    }
}
