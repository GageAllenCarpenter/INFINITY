import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.io.FileNotFoundException;
import java.util.List;


/**
 * Registers that an event has taken place in the discord server and calls on the other classes
 */
public class ProductAttachment extends ListenerAdapter {

    ProductReader start;
    String documentation = "https://gtrades.io/";
    String support = "https://gtrades.io/";
    String coverImage = "https://cdn.discordapp.com/attachments/945046449484361731/957290310935904276/Capture.png";
    String cornerImage = "https://cdn.discordapp.com/attachments/945046449484361731/957302568864870472/Capture-removebg-preview.png";

    /**
     * Constructor for objects of class Attachment
     */
    public ProductAttachment() {
    }

    /**
     * Once an attachment with the command work is received
     * The application can perform CRUD operations in Discord
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Leads")) {
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if (attachments.isEmpty()) {
                noAttachmentEmbed(event);
            } else if (attachments.contains(".csv")) {
                attachments.get(0).downloadToFile("CSV/Leads.csv");
                try {
                    start.readTacticalArbitrageCSV(event);
                    addedAttachmentEmbed(event);
                } catch (FileNotFoundException e) {
                   errorAttachmentEmbed(event);
                    ;
                }
            }
        }
    }

    /**
     * Sends an embedded message if no attachment was found
     * @param event
     */
    public void noAttachmentEmbed(MessageReceivedEvent event)
    {
        EmbedBuilder empty = new EmbedBuilder();
        MessageChannel emptyAttachment = event.getChannel();
        empty.setColor(0x3b5998);
        empty.addField("Reason","Attachment Not Found", false);
        empty.setTitle("REQUEST FAILED");
        empty.addField("Documentation",documentation, true);
        empty.addField("Support",support, true);
        empty.setThumbnail(cornerImage);
        empty.setImage(coverImage);
        empty.setFooter("GTrades - INFINITY");
        emptyAttachment.sendMessageEmbeds(empty.build()).queue();
    }

    /**
     * Sends an embedded message once CRUD is successful
     * @param event
     */
    public void addedAttachmentEmbed(MessageReceivedEvent event)
    {
        EmbedBuilder success = new EmbedBuilder();
        MessageChannel channel = event.getChannel();
        success.setColor(0x3b5998);
        success.setTitle("FILE HAS BEEN SAVED");
        channel.sendMessageEmbeds(success.build()).queue();
    }

    /**
     *  Sends an embedded message if the file type was incorrect
     * @param event
     */
    public void errorAttachmentEmbed(MessageReceivedEvent event)
    {
        EmbedBuilder failure = new EmbedBuilder();
        MessageChannel error = event.getChannel();
        failure.setColor(0x3b5998);
        failure.setTitle("REQUEST FAILED");
        failure.addField("Reason","Incorrectly Formatted Attachment", false);
        failure.addField("Documentation",documentation, true);
        failure.addField("Support",support, true);
        failure.setThumbnail(cornerImage);
        failure.setImage(coverImage);
        failure.setFooter("GTrades - INFINITY");
        error.sendMessageEmbeds(failure.build()).queue();
    }
}
