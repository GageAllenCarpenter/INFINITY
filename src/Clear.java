import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;


public class Clear extends ListenerAdapter {



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        ProductEventHandling handle = new ProductEventHandling(event);
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase("!clear")) {
            if (args.length < 2) {
                handle.postFailure(event,1,3,4);
            }
            else {
                try {
                    List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
                    event.getTextChannel().deleteMessages(messages).queue();
                    handle.postSuccess(event,3,2,5);
                }
                catch (IllegalArgumentException e) {
                    if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
                        handle.postFailure(event, 1,3,6);
                    }
                    else {
                        handle.postFailure(event, 1,3, 7);
                    }
                }
            }
        }
    }
}