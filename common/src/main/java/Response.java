import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {
    private String message;
    private String date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
