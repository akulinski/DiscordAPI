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
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import de.btobastian.javacord.utils.handler.voice.*;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

public class Main {
    public static void main(String[] args)  {

        String token="NDE3MjcwODM2MzAzNjI2MjQx.DXQlSA.6YLmZm7A0nvJ4gLvSrQcTkpE0Ng";


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

                    }

                });

                api.registerListener(new ServerJoinListener() {
                    public void onServerJoin(DiscordAPI discordAPI, Server server) {
                      Iterator<User> it= server.getMembers().iterator();
                        System.out.println("Joind");
                      while (it.hasNext()){
                          it.next().sendMessage("someone joind",true);
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
                                tmp.sendMessage(user.getName()+" joind",true);
                            }
                            else{
                                user.sendMessage("You have joind "+voiceChannel.getName(),true);
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