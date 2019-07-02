import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        sendListOfClients(session);
        System.out.println("Socket connected:"+session.getId());
    }

    @OnMessage
    public void handleMessage(Session session, message Message)
    {
        /*if(message.split(":")[0].equals("setNickName"))
        {
            for(client Client:clients) {
                if (Client.getSession().equals(session))
                    Client.setNickName(message.split(":")[1]);

                sendListOfClientsToAll();
            }
        }
        else
            broadcastMessageToClients(message);*/
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

    public void sendListOfClients(Session session)
    {
        JSONArray activeClients = createArrayOfActiveClients();

        if(!activeClients.isEmpty())
            try {
            System.out.println(activeClients.toJSONString());
                session.getBasicRemote().sendText(activeClients.toJSONString());
            }catch(Exception e){e.printStackTrace();}

        System.out.println(activeClients);
    }

    public void sendListOfClientsToAll()
    {
        JSONArray activeClients = createArrayOfActiveClients();

        if(!activeClients.isEmpty())
            for(client Client:clients) {
                try {
                    System.out.println(activeClients.toJSONString());
                    Client.getSession().getAsyncRemote().sendText(activeClients.toJSONString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        System.out.println(activeClients);
    }

    public JSONArray createArrayOfActiveClients()
    {
        JSONArray arrayOfClients = new JSONArray();
        for(client Client:clients)
        {
            if(Client.getNickName()!=null) {
                JSONObject clientJSON = new JSONObject();
                clientJSON.put("sessionId", Client.getSession().getId());
                clientJSON.put("nickName", Client.getNickName());
                arrayOfClients.add(clientJSON);
            }
        }

        return arrayOfClients;
    }

}