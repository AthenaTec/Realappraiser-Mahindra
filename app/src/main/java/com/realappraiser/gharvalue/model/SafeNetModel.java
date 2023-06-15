package com.realappraiser.gharvalue.model;

import java.util.ArrayList;
import java.util.List;

public class SafeNetModel {
    private String nonce = "";
    private Long timestampMs = 0L;
    private String apkPackageName = "";
    private String apkDigestSha256 = "";
    private Boolean ctsProfileMatch = false;
    private List<String> apkCertificateDigestSha256 = new ArrayList<>();
    private Boolean basicIntegrity = false;
    private String evaluationType = "";
    public String getNonce() {
        return nonce;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public Long getTimestampMs() {
        return timestampMs;
    }
    public void setTimestampMs(Long timestampMs) {
        this.timestampMs = timestampMs;
    }
    public String getApkPackageName() {
        return apkPackageName;
    }
    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }
    public String getApkDigestSha256() {
        return apkDigestSha256;
    }
    public void setApkDigestSha256(String apkDigestSha256) {
        this.apkDigestSha256 = apkDigestSha256;
    }
    public Boolean getCtsProfileMatch() {
        return ctsProfileMatch;
    }
    public void setCtsProfileMatch(Boolean ctsProfileMatch) {
        this.ctsProfileMatch = ctsProfileMatch;
    }
    public List<String> getApkCertificateDigestSha256() {
        return apkCertificateDigestSha256;
    }
    public void setApkCertificateDigestSha256(List<String> apkCertificateDigestSha256) {
        this.apkCertificateDigestSha256 = apkCertificateDigestSha256;
    }
    public Boolean getBasicIntegrity() {
        return basicIntegrity;
    }
    public void setBasicIntegrity(Boolean basicIntegrity) {
        this.basicIntegrity = basicIntegrity;
    }
    public String getEvaluationType() {
        return evaluationType;
    }
    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }
}
