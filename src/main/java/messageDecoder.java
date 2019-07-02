import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class messageDecoder implements Decoder.Text<message> {
    private static Gson gson = new Gson();

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public message decode(String s) throws DecodeException {
        return gson.fromJson(s, message.class);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
