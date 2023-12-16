package com.example.demowithtests.web.document;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.DocumentHistory;
import com.example.demowithtests.dto.DocumentDto;
import com.example.demowithtests.dto.DocumentHistoryDto;
import com.example.demowithtests.service.document.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DocumentController implements DocumentResource {

    private final DocumentService documentService;

    /**
     * @param document
     * @return
     */
    @Override
    @PostMapping("/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public Document createDocument(@RequestBody Document document) {
        return documentService.create(document);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @GetMapping("/documents/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DocumentDto getDocumentById(@PathVariable Integer id) {
        Document document = documentService.getById(id);
        return convertToDocumentDto(document);
    }

    @GetMapping("/documents/{id}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<DocumentHistoryDto> getDocumentHistory(@PathVariable Integer id) {
        Document document = documentService.getById(id);

        return convertToDocumentHistoryDtoList(document.getHistory());
    }


    private DocumentDto convertToDocumentDto(Document document) {
        return new DocumentDto(
                document.getId(),
                document.getName(),
                document.getType(),
                document.getNumber(),
                document.getExpireDate(),
                convertToDocumentHistoryDtoList(document.getHistory())
        );
    }


    private List<DocumentHistoryDto> convertToDocumentHistoryDtoList(List<DocumentHistory> historyList) {

        return historyList.stream()
                .map(history -> new DocumentHistoryDto(
                        history.getId(),
                        history.getTimestamp(),
                        history.getDocument().getId(),
                        history.getAction()
                ))
                .collect(Collectors.toList());
    }
}