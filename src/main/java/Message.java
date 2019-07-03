import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String sender;
    private String destination;
    private String content;
    private String operation;
}
