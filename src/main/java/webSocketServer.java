import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;


@ServerEndpoint(value="/actions",decoders = messageDecoder.class,encoders = messageEncoder.class)
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
    public void handleMessage(Session session, Message message)
    {
       for(client Client:clients)
           if(Client.getSession().getId()==session.getId())
           {
               message.setSender(Client.getNickName());
           }

        String operationToExecute = message.getOperation();

        if(operationToExecute.equals("setNickname"))
        {
            for(client Client:clients) {
                if (Client.getSession().equals(session))
                    Client.setNickName(message.getContent());


            }
            sendListOfClientsToAll();
        }
        else if(operationToExecute.equals("sendMessage"))
            broadcastMessageToClients(message);
    }

    public void connectFromClient(Session session)
    {
        client Client = new client();
        Client.setSession(session);
        clients.add(Client);
    }

    public void broadcastMessageToClients(Message message)
    {
        String destination = message.getDestination();
        if(destination.equals("Main")) {
            for (client Client : clients) {
                Session session = Client.getSession();
                if (session != null && session.isOpen())
                    session.getAsyncRemote().sendObject(message);
            }
        }
        else{
            for (client Client : clients) {
                if(Client.getNickName().equals(destination)) {
                    Session session = Client.getSession();
                    if (session != null && session.isOpen())
                        session.getAsyncRemote().sendObject(message);
                }
            }
        }
    }

    public void sendListOfClients(Session session)
    {
        JSONArray activeClients = createArrayOfActiveClients();

        if(!activeClients.isEmpty())
            try {
            System.out.println(activeClients.toJSONString());
                Message Message = new Message();
                Message.setContent(activeClients.toJSONString());
                Message.setOperation("listOfClients");
                session.getBasicRemote().sendObject(Message);
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
                    Message Message = new Message();
                    Message.setContent(activeClients.toJSONString());
                    Message.setOperation("listOfClients");
                    Client.getSession().getAsyncRemote().sendObject(Message);
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