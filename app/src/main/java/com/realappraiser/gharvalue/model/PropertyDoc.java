package com.realappraiser.gharvalue.model;

public class PropertyDoc {
    int Id,CaseId;
    String Document, DocumentName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCaseId() {
        return CaseId;
    }

    public void setCaseId(int caseId) {
        CaseId = caseId;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        Document = document;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }
}
