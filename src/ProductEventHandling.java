import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

/**
 *  Creates exceptions that post directly into discord rather than the local terminal to provide the user with instructions and more
 * @author Gage Carpenter
 * @version 1
 */
public class ProductEventHandling {
    private final String coverImage = "https://cdn.discordapp.com/attachments/945046449484361731/957290310935904276/Capture.png";
    private final String cornerImage = "https://cdn.discordapp.com/attachments/945046449484361731/957302568864870472/Capture-removebg-preview.png";
    private final String social = "https://gtrades.io/";

    /**
     * Constructor for objects of class ProductEventHandling
     */
    public ProductEventHandling(MessageReceivedEvent event) {
    }

    /**
     * Posts an embedded message once a successful request has been fulfilled
     */
    public void postSuccess(MessageReceivedEvent event, int title, int subtitle, int message) {
        EmbedBuilder success = new EmbedBuilder();
        MessageChannel channel = event.getChannel();
        success.setColor(0xff9900);
        success.setTitle(getTitle(title));
        success.addField(getSubTitle(subtitle), getMessage(message), false);
        success.setThumbnail(cornerImage);
        success.setImage(coverImage);
        success.setFooter("GTrades - INFINITY" + social);
        channel.sendMessageEmbeds(success.build()).setActionRow(postButtons()).queue();
    }

    /**
     * Posts an embedded message once a failed request has been created
     */
    public void postFailure(MessageReceivedEvent event, int title, int subtitle, int message) {
        EmbedBuilder failure = new EmbedBuilder();
        MessageChannel error = event.getChannel();
        failure.setColor(0xff9900);
        failure.setTitle(getTitle(title));
        failure.addField(getSubTitle(subtitle), getMessage(message), false);
        failure.setThumbnail(cornerImage);
        failure.setImage(coverImage);
        failure.setFooter("GTrades - INFINITY " + social);
        error.sendMessageEmbeds(failure.build()).setActionRow(postButtons()).queue();
    }

    /**
     *
     */
    public List<Button> postButtons() {
        String documentationURL = "https://gtrades.io/";
        String supportURL = "https://gtrades.io/";
        String fileURL = "https://tinyurl.com/55kre2rc";
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.link(documentationURL, "Documentation"));
        buttons.add(Button.link(supportURL, "Support"));
        buttons.add(Button.link(fileURL, "File Format"));
        return buttons;
    }


    /**
     * @param title
     * @return String
     */
    public String getTitle(int title) {
        switch (title) {
            case 1:
                return "REQUEST FAILED";
            case 2:
                return "FILE HAS BEEN SAVED";
            case 3:
                return "MESSAGES HAVE BEEN DELETED";
        }
        return null;
    }

    /**
     *
     * @param subtitle
     * @return
     */
    public String getSubTitle(int subtitle) {
        switch (subtitle) {
            case 1:
                return "Attachment Added Successfully";
            case 2:
                return "Removal Successful";
            case 3:
                return "Reason";
        }
        return null;
    }

    /**
     *
     * @param message
     * @return
     */
    public String getMessage(int message) {
        switch (message) {
            case 1:
                return "No Attachment Found";
            case 2:
                return "Products Posting Briefly";
            case 3:
                return "Incorrect File Type";
            case 4:
                return "Amount to delete not provided";
            case 5:
                return "All the messages have now been erased";
            case 6:
                return "Between 1-100 messages can be deleted at one time";
            case 7:
                return "Messages older than 2 weeks cannot be deleted";
        }
        return null;
    }
}
