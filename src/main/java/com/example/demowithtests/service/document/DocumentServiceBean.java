package com.example.demowithtests.service.document;

import com.example.demowithtests.domain.Document;
import com.example.demowithtests.domain.DocumentHistory;
import com.example.demowithtests.dto.DocumentDto;
import com.example.demowithtests.repository.DocumentHistoryRepository;
import com.example.demowithtests.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceBean implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentHistoryRepository documentHistoryRepository;

    /**
     * @param document
     * @return
     */
    @Override
    public Document create(Document document) {
        document.setExpireDate(LocalDateTime.now().plusYears(5));
        Document savedDocument = documentRepository.save(document);

        // Update history for document creation
        DocumentHistory history = DocumentHistory.builder()
                .timestamp(LocalDateTime.now())
                .document(savedDocument)
                .action("ADD")
                .build();
        documentHistoryRepository.save(history);

        return savedDocument;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document getById(Integer id) {
        return documentRepository.findById(id).orElseThrow();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Document handlePassport(Integer id) {
        Document document = getById(id);

        if (document.getIsHandled()) {
            throw new RuntimeException("Document has already been handled");
        } else {
            document.setIsHandled(Boolean.TRUE);
            document = documentRepository.save(document);
            DocumentHistory history = DocumentHistory.builder()
                    .timestamp(LocalDateTime.now())
                    .document(document)
                    .action("HANDLE_PASSPORT")
                    .build();
            documentHistoryRepository.save(history);
            return document;
        }
    }

    /**
     * @param passportId
     * @param imageId
     * @return
     */
    @Override
    public Document addImage(Integer passportId, Integer imageId) {
        return null;
    }

    @Override
    public Document delete(Integer id) {
        Document document = getById(id);
        documentRepository.delete(document);

        // Update history for document deletion
        DocumentHistory history = DocumentHistory.builder()
                .timestamp(LocalDateTime.now())
                .document(document)
                .action("DELETE")
                .build();
        documentHistoryRepository.save(history);
        return document;
    }
}
