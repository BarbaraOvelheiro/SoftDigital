package pt.ipg.a.softdigital;

public class UploadPdf {

    public String documentID;
    public String documentName;
    public String documentUrl;
    public String userID;

    public UploadPdf()  {

    }

    public UploadPdf(String documentID, String documentName, String documentUrl, String userID) {
        this.documentID = documentID;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
        this.userID = userID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
