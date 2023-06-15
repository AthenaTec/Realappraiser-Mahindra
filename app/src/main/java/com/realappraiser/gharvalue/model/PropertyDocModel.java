package com.realappraiser.gharvalue.model;

public class PropertyDocModel {

    private int caseId;
    private String filleName,filePath,file;

    public PropertyDocModel(int caseId, String file, String filleName, String filePath) {
        this.caseId = caseId;
        this.file = file;
        this.filleName = filleName;
        this.filePath = filePath;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilleName() {
        return filleName;
    }

    public void setFilleName(String filleName) {
        this.filleName = filleName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
