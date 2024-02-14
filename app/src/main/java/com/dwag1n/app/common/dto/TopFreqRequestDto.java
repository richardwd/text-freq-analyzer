package com.dwag1n.app.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: Duo Wang
 * @version: v1.0
 */
@Data
@Schema(name = "TopFreqRequestDto", description = "TopFreqRequestDto")
public class TopFreqRequestDto {
    @Schema(description = "The name of the txt file without suffix", example = "MyTxtFile")
    @NotBlank(message = "The txt file name cannot be empty")
    private String txtFileName;

    @Schema(description = "The number of top frequency words", example = "10")
    @NotNull(message = "The number of top frequency words cannot be empty")
    private int k;
}
