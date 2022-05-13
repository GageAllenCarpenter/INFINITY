import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class Buttons extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Buttons")) {
            event.getChannel().sendMessage("Test").setActionRow(sendButton()).queue();
        }
    }

    private static List<Button> sendButton()
    {

        List <Button> buttons = new ArrayList<>();
        //buttons.add(Button.primary("1","1").withUrl());
        buttons.add(Button.link("https://workona.com/0/","Amazon"));
        buttons.add(Button.link("https://workona.com/0/","Retail"));
        buttons.add(Button.link("https://workona.com/0/","Seller Application"));
        return buttons;
    }
}
