import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Stack;

public class MessageController {
    private Message message;
    private DiscordAPI api;
    private Stack<User> connected;

    MessageController(Message msg,DiscordAPI api,Stack<User> conncted){
        this.message=msg;
        this.api=api;
        this.connected=conncted;
    }
    public void run(){
        if(message.getContent().equals("kim jest tomek")) {
            message.reply("Tomasz to chuj");

        }

        if(message.getContent().contains("*whois ")){
            String name=message.getContent().substring(7,message.getContent().length());
            for (User x:
                    api.getUsers()) {
                if(x.getName().toString().equals(name)){
                    int year = x.getCreationDate().get(Calendar.YEAR);
                    int month = x.getCreationDate().get(Calendar.MONTH);
                    int day = x.getCreationDate().get(Calendar.DAY_OF_MONTH);
                    message.reply(x.getName()+" "+"Game: "+x.getGame()+" "+"Bot: "+x.isBot()+" "+"Date: "+day+"-"+month+"-"+year);
                }
            }
        }

        if(message.getContent().equals("*last") && !connected.empty()){
            message.reply(connected.peek().getName());
        }

        if(message.getContent().contains("*kick")){
            System.out.println("kick if");
            kick(message.getContent().substring(6));
        }
    }

    private void kick(String user){
        System.out.println("kick functio");
        findUser(user);
    }

    private Server findUser(String toFind){
        Iterator<Server> it=this.api.getServers().iterator();

        System.out.println("FINDUSER");

        //Iterate over all channels
        while (it.hasNext()){
            Server tmp=it.next();
            System.out.println(tmp.getName());
            //iterate over members of channel
            Iterator<User> userIterator= tmp.getMembers().iterator();
            while (userIterator.hasNext()){
                User tmpUser=userIterator.next();
                //it user == user to kick -> return
                System.out.println(tmpUser.getName());
                if(tmpUser.getName().equals(toFind)){
                    System.out.println("FOund "+tmpUser.getName());
                    tmp.kickUser(tmpUser.getId());
                    break;
            }
            }

        }
        return null;
    }
}
