package pt.ipg.a.softdigital;

public class DocumentStatus {

    private String status;
    private String statusID;
    private String documentID;

    public DocumentStatus(){

    }

    public DocumentStatus(String status, String statusID, String documentID) {
        this.status = status;
        this.statusID = statusID;
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public DocumentStatus(String status) {
        this.status = status;
    }
}
