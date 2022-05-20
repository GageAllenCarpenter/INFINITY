import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileNotFoundException;

public class Post{

    Read read;
    private EmbedBuilder products = new EmbedBuilder();
    private EmbedBuilder historicalData = new EmbedBuilder();

    /**
     * Constructor for objects of class Post
     * @param event
     * @throws FileNotFoundException
     */
    public Post(MessageReceivedEvent event) throws FileNotFoundException
    {
        productPost(event);
        historicalDataPost(event);
    }

    public void productPost(MessageReceivedEvent event) throws FileNotFoundException
    {

    }

    public void historicalDataPost(MessageReceivedEvent event) throws FileNotFoundException
    {

    }
}
