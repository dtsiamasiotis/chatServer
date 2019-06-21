import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;


@ServerEndpoint("/actions")
public class webSocketServer{

    private static List<client> clients = new ArrayList<client>();

    @OnOpen
    public void handleOpen(Session session)
    {
        connectFromClient(session);
        System.out.println("Socket connected:"+session.getId());
    }

    @OnMessage
    public void handleMessage(Session session, String message)
    {
        broadcastMessageToClients(message);
    }

    public void connectFromClient(Session session)
    {
        client Client = new client();
        Client.setSession(session);
        clients.add(Client);
    }

    public void broadcastMessageToClients(String message)
    {
        for(client Client:clients)
        {
            Session session = Client.getSession();
            if(session!=null && session.isOpen())
                session.getAsyncRemote().sendText(message);
        }
    }


}