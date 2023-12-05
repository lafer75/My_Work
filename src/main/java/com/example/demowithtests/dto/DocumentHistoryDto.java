package com.example.demowithtests.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record DocumentHistoryDto(

        @Schema(description = "Id in DB")
        Integer id,

        @Schema(description = "Timestamp of the document history.")
        LocalDateTime timestamp,

        @Schema(description = "Id of the associated document.")
        Integer documentId,

        @Schema(description = "Action performed in the document history.")
        String action) {

    public DocumentHistoryDto(Integer id,
                              LocalDateTime timestamp,
                              Integer documentId,
                              String action) {
        this.id = id;
        this.timestamp = timestamp;
        this.documentId = documentId;
        this.action = action;
    }
}
