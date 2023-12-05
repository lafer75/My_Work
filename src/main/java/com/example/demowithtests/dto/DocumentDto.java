package com.example.demowithtests.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record DocumentDto(

        @Schema(description = "Id in DB")
        Integer id,

        @Schema(description = "Name of a document.", example = "Passport", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Type of a document.", example = "Official", requiredMode = Schema.RequiredMode.REQUIRED)
        String type,

        @Schema(description = "Document number.", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
        String number,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "Expiration date of the document.")
        LocalDateTime expireDate,

        @Schema(description = "List of document history.")
        List<DocumentHistoryDto> history) {

    public DocumentDto(Integer id,
                       String name,
                       String type,
                       String number,
                       LocalDateTime expireDate,
                       List<DocumentHistoryDto> history) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.number = number;
        this.expireDate = expireDate;
        this.history = history;
    }

}
