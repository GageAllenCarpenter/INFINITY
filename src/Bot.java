import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Bot extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException, IOException
    {
        JDA jda = JDABuilder.createDefault("OTUwNjA5MjAyNDk2Mjc4NTQ4.GFM52Q.nFQLzWhQjEgWMyrOwytFUO6Ry5ukfDVUpcnCtk")
                //Listens for command words
                .addEventListeners(new Bot())
                .addEventListeners(new Ping())
                .addEventListeners(new TacticalArbitrage())
                .addEventListeners(new SandBox())
                //Discord Status
                .setActivity(Activity.playing("Beta Testing"))
                .build();
    }
}