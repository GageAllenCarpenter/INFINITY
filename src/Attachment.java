import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;

/**
 * Registers that an event has taken place in the discord server and calls on the other classes
 */
public class Attachment extends ListenerAdapter {

    public Attachment() throws FileNotFoundException {}

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!TA"))
        {
            try {
                Read start = new Read();
                start.readTacticalArbitrageCSV(event);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

