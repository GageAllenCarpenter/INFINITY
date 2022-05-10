import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Basic Message Recieved Test class used to ensure that the bot is able to listen to a channel within discord
 */
public class Ping extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Ready"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Try me").queue();
        }
    }
}
