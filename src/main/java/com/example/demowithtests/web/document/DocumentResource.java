package com.example.demowithtests.web.document;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.dto.DocumentDto;
import com.example.demowithtests.dto.DocumentHistoryDto;

import java.util.List;


public interface DocumentResource {

    Document createDocument(Document document);

    DocumentDto getDocumentById(Integer id);

    List<DocumentHistoryDto> getDocumentHistory (Integer id);
}
