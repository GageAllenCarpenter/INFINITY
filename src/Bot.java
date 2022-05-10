import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException
    {
        JDA jda = JDABuilder.createDefault("OTUwNjA5MjAyNDk2Mjc4NTQ4.GFM52Q.nFQLzWhQjEgWMyrOwytFUO6Ry5ukfDVUpcnCtk")
                //Listens for command words
                .addEventListeners(new Bot())
                .addEventListeners(new Ping())
                .addEventListeners(new TacticalArbitrage())
                //Discord Status
                .setActivity(Activity.playing("Soon"))
                .build();
    }
}