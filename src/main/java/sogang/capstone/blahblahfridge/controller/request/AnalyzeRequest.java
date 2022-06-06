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
public class AnalyzeRequest {

    @NotNull(message = "bucket may not be null")
    String bucket;
    @NotNull(message = "key may not be null")
    String key;
}
