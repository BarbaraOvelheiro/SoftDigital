package pt.ipg.a.softdigital;

public class DocumentStatus {

    private String status;
    private String statusID;
    private String documentID;
    private String userID;
    private String messageID;

    public DocumentStatus(){

    }

    public DocumentStatus(String status, String statusID, String documentID, String userID, String messageID) {
        this.status = status;
        this.statusID = statusID;
        this.documentID = documentID;
        this.userID = userID;
        this.messageID = messageID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
