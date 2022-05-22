import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.FileNotFoundException;


/**
 * Registers that an event has taken place in the discord server and calls on the other classes
 */
public class ProductAttachment extends ListenerAdapter {

    public ProductAttachment() {
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!TA")) {
            try {
                ProductReader start = new ProductReader();
                start.readTacticalArbitrageCSV(event);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}