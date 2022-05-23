import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.io.FileNotFoundException;
import java.util.List;


/**
 * Registers that an event has taken place in the discord server and calls on the other classes
 */
public class ProductAttachment extends ListenerAdapter {

    /**
     * Constructor for objects of class Attachment
     */
    public ProductAttachment() {}

    /**
     * Once an attachment with the command work is received
     * The application can perform CRUD operations in Discord
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        ProductEventHandling handle = new ProductEventHandling(event);
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Leads")) {
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if (attachments.isEmpty()) {
                handle.postFailure(event,1,3,1);
            } else {
                attachments.get(0).downloadToFile("CSV/Leads.csv");
                try {
                    ProductReader start = new ProductReader();
                    start.read(event);
                    handle.postSuccess(event,2,1,2);
                } catch (FileNotFoundException e) { //Needs Work
                   handle.postFailure(event, 1,3,3);
                }
            }
        }
    }
}
