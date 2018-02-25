import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import java.util.Calendar;
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

        if(message.getContent().equals("*kick")){
            kick(message.getAuthor());

        }
    }

    private void kick(User user){

    }
}
