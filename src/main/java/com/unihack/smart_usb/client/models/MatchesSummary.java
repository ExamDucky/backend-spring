package com.unihack.smart_usb.client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchesSummary {
    @JsonProperty("total_matches")
    private int totalMatches;
    @JsonProperty("traditional_matches")
    private int traditionalMatches;

    @JsonProperty("llm_matches")
    private int llmMatches;

    //{
    //  "similarity_distribution": {
    //    "traditional": {
    //      "scores": [],
    //      "average": 0
    //    },
    //    "llm": {
    //      "scores": [],
    //      "average": 0
    //    }
    //  },
    //  "matches_summary": {
    //    "total_matches": 0,
    //    "traditional_matches": 0,
    //    "llm_matches": 0
    //  }
    //}
}
