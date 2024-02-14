package com.dwag1n.app.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: Duo Wang
 * @createDate: 2/13/2024 - 7:07 PM
 * @version: v1.0
 */
@Data
@Builder
@Schema(name = "TopFreqWordsDto", description = "TopFreqWordsDto")
public class TopFreqWordsDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "The word", example = "the")
    @JsonProperty("word")
    private String word;

    @Schema(description = "The frequency of the word", example = "100")
    @JsonProperty("frequency")
    private Integer frequency;

    @Schema(description = "The rank of the word", example = "1")
    @JsonProperty("rank")
    private Integer rank;
}