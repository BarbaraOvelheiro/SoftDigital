package pt.ipg.a.softdigital;

public class UploadPdf {

    public String name;
    public String url;
    public String from;

    public UploadPdf()  {

    }

    public UploadPdf(String name, String url, String from) {
        this.name = name;
        this.url = url;
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
