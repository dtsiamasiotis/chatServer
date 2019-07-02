import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class message {
    private String sender;
    private String destination;
    private String content;
    private String operation;
}
