import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;

/**
 * The Main Method of the program that connects Java to the discord JDA API using a Token
 */
public class Main extends ListenerAdapter {
    public static void main(String[] args) throws FileNotFoundException, LoginException {
        JDA jda = JDABuilder.createDefault("OTUwNjA5MjAyNDk2Mjc4NTQ4.GFM52Q.nFQLzWhQjEgWMyrOwytFUO6Ry5ukfDVUpcnCtk")
                .addEventListeners(new Attachment())
                .setActivity(Activity.playing("Am I still Alive"))
                .build();
    }
}
