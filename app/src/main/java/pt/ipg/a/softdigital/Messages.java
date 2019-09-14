package pt.ipg.a.softdigital;

public class Messages {

    private String messageID, documentName, documentUrl, userFromID, userToID, statusID, documentID;

    public Messages(){

    }

    public Messages(String messageID, String documentName, String documentUrl, String userFromID, String userToID, String statusID, String documentID) {
        this.messageID = messageID;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
        this.userFromID = userFromID;
        this.userToID = userToID;
        this.statusID = statusID;
        this.documentID = documentID;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getStatusID() {
        return statusID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getUserToID() {
        return userToID;
    }

    public void setUserToID(String receiver) {
        this.userToID = userToID;
    }

    public String getUserFromID() {
        return userFromID;
    }

    public void setUserFromID(String userFromID) {
        this.userFromID = userFromID;
    }
}
