package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadQcModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Data {

        @SerializedName("CaseId")
        @Expose
        private Integer caseId;
        @SerializedName("AgencyBranchId")
        @Expose
        private Integer agencyBranchId;
        @SerializedName("BankBranchId")
        @Expose
        private Integer bankBranchId;
        @SerializedName("PropertyId")
        @Expose
        private Integer propertyId;
        @SerializedName("CustomerId")
        @Expose
        private Integer customerId;
        @SerializedName("FeesId")
        @Expose
        private Integer feesId;
        @SerializedName("WorkflowId")
        @Expose
        private Object workflowId;
        @SerializedName("ReportId")
        @Expose
        private Integer reportId;
        @SerializedName("ReportTemplateId")
        @Expose
        private Integer reportTemplateId;
        @SerializedName("AgencyBranch")
        @Expose
        private Object agencyBranch;
        @SerializedName("ApplicantName")
        @Expose
        private String applicantName;
        @SerializedName("ApplicantContactNo")
        @Expose
        private Object applicantContactNo;
        @SerializedName("ContactPersonName")
        @Expose
        private Object contactPersonName;
        @SerializedName("ContactPersonNumber")
        @Expose
        private Object contactPersonNumber;
        @SerializedName("BankContactPersonName")
        @Expose
        private Object bankContactPersonName;
        @SerializedName("BankContactPersonNumber")
        @Expose
        private Object bankContactPersonNumber;
        @SerializedName("BankContactPersonEmail")
        @Expose
        private Object bankContactPersonEmail;
        @SerializedName("LoanType")
        @Expose
        private String loanType;
        @SerializedName("BankReferenceNO")
        @Expose
        private Object bankReferenceNO;
        @SerializedName("ApplicantEmailId")
        @Expose
        private Object applicantEmailId;
        @SerializedName("ApplicantAddress")
        @Expose
        private String applicantAddress;
        @SerializedName("ReportType")
        @Expose
        private Integer reportType;
        @SerializedName("ReportFile")
        @Expose
        private Integer reportFile;
        @SerializedName("BankName")
        @Expose
        private Object bankName;
        @SerializedName("BranchName")
        @Expose
        private Object branchName;
        @SerializedName("PropertyAddress")
        @Expose
        private String propertyAddress;
        @SerializedName("Pincode")
        @Expose
        private Object pincode;
        @SerializedName("PropertyType")
        @Expose
        private Integer propertyType;
        @SerializedName("ProfessionalFees")
        @Expose
        private Object professionalFees;
        @SerializedName("PropertyCity")
        @Expose
        private Integer propertyCity;
        @SerializedName("PropertyLocality")
        @Expose
        private Object propertyLocality;
        @SerializedName("VillageName")
        @Expose
        private Object villageName;
        @SerializedName("District")
        @Expose
        private Object district;
        @SerializedName("Taluka")
        @Expose
        private Object taluka;
        @SerializedName("Purpose")
        @Expose
        private Object purpose;
        @SerializedName("TaxType")
        @Expose
        private Object taxType;
        @SerializedName("Status")
        @Expose
        private Integer status;
        @SerializedName("AssignedTo")
        @Expose
        private Integer assignedTo;
        @SerializedName("AssignedBy")
        @Expose
        private Integer assignedBy;
        @SerializedName("AssignedAt")
        @Expose
        private String assignedAt;
        @SerializedName("AmountReceived")
        @Expose
        private Float amountReceived;
        @SerializedName("FieldStaff")
        @Expose
        private Integer fieldStaff;
        @SerializedName("ReportMaker")
        @Expose
        private Integer reportMaker;
        @SerializedName("ReportMakerDate")
        @Expose
        private String reportMakerDate;
        @SerializedName("ReportChecker")
        @Expose
        private Object reportChecker;
        @SerializedName("ReportFinalizer")
        @Expose
        private Object reportFinalizer;
        @SerializedName("ReportDispatcher")
        @Expose
        private Object reportDispatcher;
        @SerializedName("Telecaller")
        @Expose
        private Object telecaller;
        @SerializedName("CaseSourced")
        @Expose
        private Object caseSourced;
        @SerializedName("SourcedBy")
        @Expose
        private Object sourcedBy;
        @SerializedName("GharvalueCity")
        @Expose
        private Object gharvalueCity;
        @SerializedName("GharvalueLocality")
        @Expose
        private Object gharvalueLocality;
        @SerializedName("GharvaluePropertyName")
        @Expose
        private Object gharvaluePropertyName;
        @SerializedName("CustomerFeedbackReceived")
        @Expose
        private Object customerFeedbackReceived;
        @SerializedName("BankFeedbackReceived")
        @Expose
        private Object bankFeedbackReceived;
        @SerializedName("CaseReferenceNumber")
        @Expose
        private String caseReferenceNumber;
        @SerializedName("FooterReferenceNumber")
        @Expose
        private String footerReferenceNumber;
        @SerializedName("AppointmentDateTime")
        @Expose
        private String appointmentDateTime;
        @SerializedName("SubmittedTo")
        @Expose
        private Object submittedTo;
        @SerializedName("CreatedOn")
        @Expose
        private String createdOn;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("ModifiedOn")
        @Expose
        private String modifiedOn;
        @SerializedName("ModifiedBy")
        @Expose
        private Integer modifiedBy;
        @SerializedName("Signature1")
        @Expose
        private Object signature1;
        @SerializedName("Signature2")
        @Expose
        private Object signature2;
        @SerializedName("AcceptanceTime")
        @Expose
        private Float acceptanceTime;
        @SerializedName("InspectionTime")
        @Expose
        private Float inspectionTime;
        @SerializedName("IsFromTab")
        @Expose
        private Object isFromTab;
        @SerializedName("ShareFolder")
        @Expose
        private Object shareFolder;
        @SerializedName("ReportGenerationTime")
        @Expose
        private Object reportGenerationTime;
        @SerializedName("CAEmpId")
        @Expose
        private String cAEmpId;
        @SerializedName("TypeofOwnership")
        @Expose
        private Object typeofOwnership;
        @SerializedName("ApprovedPlanNumber")
        @Expose
        private Object approvedPlanNumber;
        @SerializedName("ApprovedPlanDate")
        @Expose
        private Object approvedPlanDate;
        @SerializedName("ApprovedPlanApprovingAuthority")
        @Expose
        private Object approvedPlanApprovingAuthority;
        @SerializedName("ApprovedPlanPreparedBy")
        @Expose
        private Object approvedPlanPreparedBy;
        @SerializedName("CommencementCertificateNumber")
        @Expose
        private Object commencementCertificateNumber;
        @SerializedName("CommencementCertificateDate")
        @Expose
        private Object commencementCertificateDate;
        @SerializedName("CommencementApprovingAuthority")
        @Expose
        private Object commencementApprovingAuthority;
        @SerializedName("CommencementCertificatePreparedBy")
        @Expose
        private Object commencementCertificatePreparedBy;
        @SerializedName("ApprovedRoleID")
        @Expose
        private Object approvedRoleID;
        @SerializedName("SubBranch")
        @Expose
        private Integer subBranch;
        @SerializedName("AssignedBranchId")
        @Expose
        private Integer assignedBranchId;
        @SerializedName("ApprovalNote")
        @Expose
        private Object approvalNote;
        @SerializedName("RejectionNote")
        @Expose
        private Object rejectionNote;
        @SerializedName("QCNote")
        @Expose
        private String qCNote;
        @SerializedName("QCAssignedOn")
        @Expose
        private String qCAssignedOn;
        @SerializedName("QCStatus")
        @Expose
        private Boolean qCStatus;
        @SerializedName("CAAgencyBranchId")
        @Expose
        private Object cAAgencyBranchId;
        @SerializedName("FSAgencyBranchId")
        @Expose
        private Object fSAgencyBranchId;
        @SerializedName("RMAgencyBranchId")
        @Expose
        private Object rMAgencyBranchId;
        @SerializedName("RMStatus")
        @Expose
        private Object rMStatus;
        @SerializedName("RMNote")
        @Expose
        private Object rMNote;
        @SerializedName("RMAssignedOn")
        @Expose
        private Object rMAssignedOn;
        @SerializedName("RMAssignedBy")
        @Expose
        private Object rMAssignedBy;
        @SerializedName("RMQCNote")
        @Expose
        private Object rMQCNote;
        @SerializedName("RMQCStatus")
        @Expose
        private Object rMQCStatus;
        @SerializedName("RMQCAssignedOn")
        @Expose
        private Object rMQCAssignedOn;
        @SerializedName("ApprovalId")
        @Expose
        private Object approvalId;

        public Integer getCaseId() {
            return caseId;
        }

        public void setCaseId(Integer caseId) {
            this.caseId = caseId;
        }

        public Integer getAgencyBranchId() {
            return agencyBranchId;
        }

        public void setAgencyBranchId(Integer agencyBranchId) {
            this.agencyBranchId = agencyBranchId;
        }

        public Integer getBankBranchId() {
            return bankBranchId;
        }

        public void setBankBranchId(Integer bankBranchId) {
            this.bankBranchId = bankBranchId;
        }

        public Integer getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(Integer propertyId) {
            this.propertyId = propertyId;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public Integer getFeesId() {
            return feesId;
        }

        public void setFeesId(Integer feesId) {
            this.feesId = feesId;
        }

        public Object getWorkflowId() {
            return workflowId;
        }

        public void setWorkflowId(Object workflowId) {
            this.workflowId = workflowId;
        }

        public Integer getReportId() {
            return reportId;
        }

        public void setReportId(Integer reportId) {
            this.reportId = reportId;
        }

        public Integer getReportTemplateId() {
            return reportTemplateId;
        }

        public void setReportTemplateId(Integer reportTemplateId) {
            this.reportTemplateId = reportTemplateId;
        }

        public Object getAgencyBranch() {
            return agencyBranch;
        }

        public void setAgencyBranch(Object agencyBranch) {
            this.agencyBranch = agencyBranch;
        }

        public String getApplicantName() {
            return applicantName;
        }

        public void setApplicantName(String applicantName) {
            this.applicantName = applicantName;
        }

        public Object getApplicantContactNo() {
            return applicantContactNo;
        }

        public void setApplicantContactNo(Object applicantContactNo) {
            this.applicantContactNo = applicantContactNo;
        }

        public Object getContactPersonName() {
            return contactPersonName;
        }

        public void setContactPersonName(Object contactPersonName) {
            this.contactPersonName = contactPersonName;
        }

        public Object getContactPersonNumber() {
            return contactPersonNumber;
        }

        public void setContactPersonNumber(Object contactPersonNumber) {
            this.contactPersonNumber = contactPersonNumber;
        }

        public Object getBankContactPersonName() {
            return bankContactPersonName;
        }

        public void setBankContactPersonName(Object bankContactPersonName) {
            this.bankContactPersonName = bankContactPersonName;
        }

        public Object getBankContactPersonNumber() {
            return bankContactPersonNumber;
        }

        public void setBankContactPersonNumber(Object bankContactPersonNumber) {
            this.bankContactPersonNumber = bankContactPersonNumber;
        }

        public Object getBankContactPersonEmail() {
            return bankContactPersonEmail;
        }

        public void setBankContactPersonEmail(Object bankContactPersonEmail) {
            this.bankContactPersonEmail = bankContactPersonEmail;
        }

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public Object getBankReferenceNO() {
            return bankReferenceNO;
        }

        public void setBankReferenceNO(Object bankReferenceNO) {
            this.bankReferenceNO = bankReferenceNO;
        }

        public Object getApplicantEmailId() {
            return applicantEmailId;
        }

        public void setApplicantEmailId(Object applicantEmailId) {
            this.applicantEmailId = applicantEmailId;
        }

        public String getApplicantAddress() {
            return applicantAddress;
        }

        public void setApplicantAddress(String applicantAddress) {
            this.applicantAddress = applicantAddress;
        }

        public Integer getReportType() {
            return reportType;
        }

        public void setReportType(Integer reportType) {
            this.reportType = reportType;
        }

        public Integer getReportFile() {
            return reportFile;
        }

        public void setReportFile(Integer reportFile) {
            this.reportFile = reportFile;
        }

        public Object getBankName() {
            return bankName;
        }

        public void setBankName(Object bankName) {
            this.bankName = bankName;
        }

        public Object getBranchName() {
            return branchName;
        }

        public void setBranchName(Object branchName) {
            this.branchName = branchName;
        }

        public String getPropertyAddress() {
            return propertyAddress;
        }

        public void setPropertyAddress(String propertyAddress) {
            this.propertyAddress = propertyAddress;
        }

        public Object getPincode() {
            return pincode;
        }

        public void setPincode(Object pincode) {
            this.pincode = pincode;
        }

        public Integer getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(Integer propertyType) {
            this.propertyType = propertyType;
        }

        public Object getProfessionalFees() {
            return professionalFees;
        }

        public void setProfessionalFees(Object professionalFees) {
            this.professionalFees = professionalFees;
        }

        public Integer getPropertyCity() {
            return propertyCity;
        }

        public void setPropertyCity(Integer propertyCity) {
            this.propertyCity = propertyCity;
        }

        public Object getPropertyLocality() {
            return propertyLocality;
        }

        public void setPropertyLocality(Object propertyLocality) {
            this.propertyLocality = propertyLocality;
        }

        public Object getVillageName() {
            return villageName;
        }

        public void setVillageName(Object villageName) {
            this.villageName = villageName;
        }

        public Object getDistrict() {
            return district;
        }

        public void setDistrict(Object district) {
            this.district = district;
        }

        public Object getTaluka() {
            return taluka;
        }

        public void setTaluka(Object taluka) {
            this.taluka = taluka;
        }

        public Object getPurpose() {
            return purpose;
        }

        public void setPurpose(Object purpose) {
            this.purpose = purpose;
        }

        public Object getTaxType() {
            return taxType;
        }

        public void setTaxType(Object taxType) {
            this.taxType = taxType;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getAssignedTo() {
            return assignedTo;
        }

        public void setAssignedTo(Integer assignedTo) {
            this.assignedTo = assignedTo;
        }

        public Integer getAssignedBy() {
            return assignedBy;
        }

        public void setAssignedBy(Integer assignedBy) {
            this.assignedBy = assignedBy;
        }

        public String getAssignedAt() {
            return assignedAt;
        }

        public void setAssignedAt(String assignedAt) {
            this.assignedAt = assignedAt;
        }

        public Float getAmountReceived() {
            return amountReceived;
        }

        public void setAmountReceived(Float amountReceived) {
            this.amountReceived = amountReceived;
        }

        public Integer getFieldStaff() {
            return fieldStaff;
        }

        public void setFieldStaff(Integer fieldStaff) {
            this.fieldStaff = fieldStaff;
        }

        public Integer getReportMaker() {
            return reportMaker;
        }

        public void setReportMaker(Integer reportMaker) {
            this.reportMaker = reportMaker;
        }

        public String getReportMakerDate() {
            return reportMakerDate;
        }

        public void setReportMakerDate(String reportMakerDate) {
            this.reportMakerDate = reportMakerDate;
        }

        public Object getReportChecker() {
            return reportChecker;
        }

        public void setReportChecker(Object reportChecker) {
            this.reportChecker = reportChecker;
        }

        public Object getReportFinalizer() {
            return reportFinalizer;
        }

        public void setReportFinalizer(Object reportFinalizer) {
            this.reportFinalizer = reportFinalizer;
        }

        public Object getReportDispatcher() {
            return reportDispatcher;
        }

        public void setReportDispatcher(Object reportDispatcher) {
            this.reportDispatcher = reportDispatcher;
        }

        public Object getTelecaller() {
            return telecaller;
        }

        public void setTelecaller(Object telecaller) {
            this.telecaller = telecaller;
        }

        public Object getCaseSourced() {
            return caseSourced;
        }

        public void setCaseSourced(Object caseSourced) {
            this.caseSourced = caseSourced;
        }

        public Object getSourcedBy() {
            return sourcedBy;
        }

        public void setSourcedBy(Object sourcedBy) {
            this.sourcedBy = sourcedBy;
        }

        public Object getGharvalueCity() {
            return gharvalueCity;
        }

        public void setGharvalueCity(Object gharvalueCity) {
            this.gharvalueCity = gharvalueCity;
        }

        public Object getGharvalueLocality() {
            return gharvalueLocality;
        }

        public void setGharvalueLocality(Object gharvalueLocality) {
            this.gharvalueLocality = gharvalueLocality;
        }

        public Object getGharvaluePropertyName() {
            return gharvaluePropertyName;
        }

        public void setGharvaluePropertyName(Object gharvaluePropertyName) {
            this.gharvaluePropertyName = gharvaluePropertyName;
        }

        public Object getCustomerFeedbackReceived() {
            return customerFeedbackReceived;
        }

        public void setCustomerFeedbackReceived(Object customerFeedbackReceived) {
            this.customerFeedbackReceived = customerFeedbackReceived;
        }

        public Object getBankFeedbackReceived() {
            return bankFeedbackReceived;
        }

        public void setBankFeedbackReceived(Object bankFeedbackReceived) {
            this.bankFeedbackReceived = bankFeedbackReceived;
        }

        public String getCaseReferenceNumber() {
            return caseReferenceNumber;
        }

        public void setCaseReferenceNumber(String caseReferenceNumber) {
            this.caseReferenceNumber = caseReferenceNumber;
        }

        public String getFooterReferenceNumber() {
            return footerReferenceNumber;
        }

        public void setFooterReferenceNumber(String footerReferenceNumber) {
            this.footerReferenceNumber = footerReferenceNumber;
        }

        public String getAppointmentDateTime() {
            return appointmentDateTime;
        }

        public void setAppointmentDateTime(String appointmentDateTime) {
            this.appointmentDateTime = appointmentDateTime;
        }

        public Object getSubmittedTo() {
            return submittedTo;
        }

        public void setSubmittedTo(Object submittedTo) {
            this.submittedTo = submittedTo;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public Integer getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Integer modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public Object getSignature1() {
            return signature1;
        }

        public void setSignature1(Object signature1) {
            this.signature1 = signature1;
        }

        public Object getSignature2() {
            return signature2;
        }

        public void setSignature2(Object signature2) {
            this.signature2 = signature2;
        }

        public Float getAcceptanceTime() {
            return acceptanceTime;
        }

        public void setAcceptanceTime(Float acceptanceTime) {
            this.acceptanceTime = acceptanceTime;
        }

        public Float getInspectionTime() {
            return inspectionTime;
        }

        public void setInspectionTime(Float inspectionTime) {
            this.inspectionTime = inspectionTime;
        }

        public Object getIsFromTab() {
            return isFromTab;
        }

        public void setIsFromTab(Object isFromTab) {
            this.isFromTab = isFromTab;
        }

        public Object getShareFolder() {
            return shareFolder;
        }

        public void setShareFolder(Object shareFolder) {
            this.shareFolder = shareFolder;
        }

        public Object getReportGenerationTime() {
            return reportGenerationTime;
        }

        public void setReportGenerationTime(Object reportGenerationTime) {
            this.reportGenerationTime = reportGenerationTime;
        }

        public String getCAEmpId() {
            return cAEmpId;
        }

        public void setCAEmpId(String cAEmpId) {
            this.cAEmpId = cAEmpId;
        }

        public Object getTypeofOwnership() {
            return typeofOwnership;
        }

        public void setTypeofOwnership(Object typeofOwnership) {
            this.typeofOwnership = typeofOwnership;
        }

        public Object getApprovedPlanNumber() {
            return approvedPlanNumber;
        }

        public void setApprovedPlanNumber(Object approvedPlanNumber) {
            this.approvedPlanNumber = approvedPlanNumber;
        }

        public Object getApprovedPlanDate() {
            return approvedPlanDate;
        }

        public void setApprovedPlanDate(Object approvedPlanDate) {
            this.approvedPlanDate = approvedPlanDate;
        }

        public Object getApprovedPlanApprovingAuthority() {
            return approvedPlanApprovingAuthority;
        }

        public void setApprovedPlanApprovingAuthority(Object approvedPlanApprovingAuthority) {
            this.approvedPlanApprovingAuthority = approvedPlanApprovingAuthority;
        }

        public Object getApprovedPlanPreparedBy() {
            return approvedPlanPreparedBy;
        }

        public void setApprovedPlanPreparedBy(Object approvedPlanPreparedBy) {
            this.approvedPlanPreparedBy = approvedPlanPreparedBy;
        }

        public Object getCommencementCertificateNumber() {
            return commencementCertificateNumber;
        }

        public void setCommencementCertificateNumber(Object commencementCertificateNumber) {
            this.commencementCertificateNumber = commencementCertificateNumber;
        }

        public Object getCommencementCertificateDate() {
            return commencementCertificateDate;
        }

        public void setCommencementCertificateDate(Object commencementCertificateDate) {
            this.commencementCertificateDate = commencementCertificateDate;
        }

        public Object getCommencementApprovingAuthority() {
            return commencementApprovingAuthority;
        }

        public void setCommencementApprovingAuthority(Object commencementApprovingAuthority) {
            this.commencementApprovingAuthority = commencementApprovingAuthority;
        }

        public Object getCommencementCertificatePreparedBy() {
            return commencementCertificatePreparedBy;
        }

        public void setCommencementCertificatePreparedBy(Object commencementCertificatePreparedBy) {
            this.commencementCertificatePreparedBy = commencementCertificatePreparedBy;
        }

        public Object getApprovedRoleID() {
            return approvedRoleID;
        }

        public void setApprovedRoleID(Object approvedRoleID) {
            this.approvedRoleID = approvedRoleID;
        }

        public Integer getSubBranch() {
            return subBranch;
        }

        public void setSubBranch(Integer subBranch) {
            this.subBranch = subBranch;
        }

        public Integer getAssignedBranchId() {
            return assignedBranchId;
        }

        public void setAssignedBranchId(Integer assignedBranchId) {
            this.assignedBranchId = assignedBranchId;
        }

        public Object getApprovalNote() {
            return approvalNote;
        }

        public void setApprovalNote(Object approvalNote) {
            this.approvalNote = approvalNote;
        }

        public Object getRejectionNote() {
            return rejectionNote;
        }

        public void setRejectionNote(Object rejectionNote) {
            this.rejectionNote = rejectionNote;
        }

        public String getQCNote() {
            return qCNote;
        }

        public void setQCNote(String qCNote) {
            this.qCNote = qCNote;
        }

        public String getQCAssignedOn() {
            return qCAssignedOn;
        }

        public void setQCAssignedOn(String qCAssignedOn) {
            this.qCAssignedOn = qCAssignedOn;
        }

        public Boolean getQCStatus() {
            return qCStatus;
        }

        public void setQCStatus(Boolean qCStatus) {
            this.qCStatus = qCStatus;
        }

        public Object getCAAgencyBranchId() {
            return cAAgencyBranchId;
        }

        public void setCAAgencyBranchId(Object cAAgencyBranchId) {
            this.cAAgencyBranchId = cAAgencyBranchId;
        }

        public Object getFSAgencyBranchId() {
            return fSAgencyBranchId;
        }

        public void setFSAgencyBranchId(Object fSAgencyBranchId) {
            this.fSAgencyBranchId = fSAgencyBranchId;
        }

        public Object getRMAgencyBranchId() {
            return rMAgencyBranchId;
        }

        public void setRMAgencyBranchId(Object rMAgencyBranchId) {
            this.rMAgencyBranchId = rMAgencyBranchId;
        }

        public Object getRMStatus() {
            return rMStatus;
        }

        public void setRMStatus(Object rMStatus) {
            this.rMStatus = rMStatus;
        }

        public Object getRMNote() {
            return rMNote;
        }

        public void setRMNote(Object rMNote) {
            this.rMNote = rMNote;
        }

        public Object getRMAssignedOn() {
            return rMAssignedOn;
        }

        public void setRMAssignedOn(Object rMAssignedOn) {
            this.rMAssignedOn = rMAssignedOn;
        }

        public Object getRMAssignedBy() {
            return rMAssignedBy;
        }

        public void setRMAssignedBy(Object rMAssignedBy) {
            this.rMAssignedBy = rMAssignedBy;
        }

        public Object getRMQCNote() {
            return rMQCNote;
        }

        public void setRMQCNote(Object rMQCNote) {
            this.rMQCNote = rMQCNote;
        }

        public Object getRMQCStatus() {
            return rMQCStatus;
        }

        public void setRMQCStatus(Object rMQCStatus) {
            this.rMQCStatus = rMQCStatus;
        }

        public Object getRMQCAssignedOn() {
            return rMQCAssignedOn;
        }

        public void setRMQCAssignedOn(Object rMQCAssignedOn) {
            this.rMQCAssignedOn = rMQCAssignedOn;
        }

        public Object getApprovalId() {
            return approvalId;
        }

        public void setApprovalId(Object approvalId) {
            this.approvalId = approvalId;
        }

    }
}
