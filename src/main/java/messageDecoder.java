import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class messageDecoder implements Decoder.Text<Message> {
    private static Gson gson = new Gson();

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public Message decode(String s) throws DecodeException {
        return gson.fromJson(s, Message.class);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
