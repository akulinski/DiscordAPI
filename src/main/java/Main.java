import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.VoiceChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import de.btobastian.javacord.listener.server.ServerJoinListener;
import de.btobastian.javacord.listener.server.ServerLeaveListener;
import de.btobastian.javacord.listener.voice.UserJoinVoiceChannelListener;
import de.btobastian.javacord.listener.voice.UserLeaveVoiceChannelListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Main {
    public static void main(String[] args)  {

        String token="NDE3MjcwODM2MzAzNjI2MjQx.DXQlSA.6YLmZm7A0nvJ4gLvSrQcTkpE0Ng";
        //swietny program
        //tomaszek świetny gość
        //tomaszek świetny gość pl
        final Stack<User> connected=new Stack<User>();
        // See "How to get the token" below
        DiscordAPI api = Javacord.getApi(token, true);
        // connect
        api.setAutoReconnect(true);

        api.connect(new FutureCallback<DiscordAPI>() {
            @Override
            public void onSuccess(final DiscordAPI api) {
                System.out.println("SUCCESS"+api.getUsers().toString());

                // register listener
                api.registerListener(new MessageCreateListener() {
                    @Override
                    public void onMessageCreate(DiscordAPI api, Message message) {
                        // check the content of the message
                        // reply to the message
                    MessageController msgc=new MessageController(message,api,connected);
                    msgc.run();

                    }

                });

                api.registerListener(new ServerJoinListener() {
                    public void onServerJoin(DiscordAPI discordAPI, Server server) {
                      Iterator<User> it= server.getMembers().iterator();
                        System.out.println("Jojned");
                      while (it.hasNext()){
                          it.next().sendMessage("someone jojned",true);


                      }
                    }
                });

                api.registerListener(new ServerLeaveListener() {
                    public void onServerLeave(DiscordAPI discordAPI, Server server) {
                        System.out.println("someone left");

                    }
                });

                api.registerListener(new UserJoinVoiceChannelListener() {
                    public void onUserJoinVoiceChannel(DiscordAPI discordAPI, User user, VoiceChannel voiceChannel) {
                        Iterator<User> it=voiceChannel.getConnectedUsers().iterator();

                        while(it.hasNext()){
                            User tmp=it.next();
                            if(tmp.getId() != user.getId()){
                                tmp.sendMessage(user.getName()+" joined",true);
                            }
                            else{
                                user.sendMessage("You have joined "+voiceChannel.getName(),true);
                            }
                        }

                        connected.push(user);
                    }

                });

                api.registerListener(new UserLeaveVoiceChannelListener() {
                    public void onUserLeaveVoiceChannel(DiscordAPI discordAPI, User user) {
                        Iterator<User> it=discordAPI.getUsers().iterator();

                        while(it.hasNext()){
                            User tmp=it.next();
                            if(tmp.getId() != user.getId()){
                                tmp.sendMessage(user.getName()+" left",true);

                            }
                        }
                    }
                });




            }


            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }

        });




    }
}