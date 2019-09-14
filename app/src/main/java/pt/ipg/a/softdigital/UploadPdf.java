package pt.ipg.a.softdigital;

public class UploadPdf {

    public String documentID;
    public String documentName;
    public String documentUrl;
    public String userID;
    public String statusID;

    public UploadPdf()  {

    }


    public UploadPdf(String documentID, String documentName, String documentUrl, String userID, String statusID) {
        this.documentID = documentID;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
        this.userID = userID;
        this.statusID = statusID;
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

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
