package com.unihack.smart_usb.client.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimilarityDistribution {

    private ScoreDetail traditional;
    private ScoreDetail llm;

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
