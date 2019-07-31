package pt.ipg.a.softdigital;

public class Messages {

    private String message, pdfurl, type, from, to;

    public Messages(){

    }


    public Messages(String message, String pdfurl, String type, String from, String to) {
        this.message = message;
        this.pdfurl = pdfurl;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String receiver) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
