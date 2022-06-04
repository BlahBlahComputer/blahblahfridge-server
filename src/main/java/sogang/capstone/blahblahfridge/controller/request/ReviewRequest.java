package sogang.capstone.blahblahfridge.controller.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    @NotNull(message = "rate may not be null")
    Integer rate;
    @NotNull(message = "content may not be null")
    String content;
    String image;
    @NotNull(message = "menuId may not be null")
    Long menuId;

}
