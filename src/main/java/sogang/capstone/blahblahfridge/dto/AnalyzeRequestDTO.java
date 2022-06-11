package sogang.capstone.blahblahfridge.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class AnalyzeRequestDTO {
    private String bucket;

    private String key;
}
