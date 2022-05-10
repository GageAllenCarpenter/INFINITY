import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TacticalArbitrage extends ListenerAdapter {

    Matcher matcher;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Leads"))
        {
            MessageChannel channel = event.getChannel();
            //Locate the CSV
            String path ="src/Tactical Arbitrage CSVs/tacticalarbitrage_1648431828.csv";
            String line ="";
            try
            {
                //Create an Embedded Post
                EmbedBuilder leads = new EmbedBuilder();
                //Concatenate The Embedded Post with the
                StringBuilder stringBuilder = new StringBuilder();
                //Read the CSV From the String File Path
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                while ((line = bufferedReader.readLine()) != null) {
                    //Determine Where Each Column of the CSV ends and a new one begins
                    //String [] values = line.split(",");
                    int i = 54;
                    String[] values = new String[i]; // size of the array, i just don't remember
                    Matcher matcher = Pattern.compile("(?<=^|,)[^,\"]*?(?=,|$)|(?<=(?:^|,)\").*?(?=\"(?:,|$))").matcher(line);
                    for (i = 0; i < values.length; i++) {
                        if (matcher.find())
                            values[i] = matcher.group();
                    }
                    stringBuilder.append(bufferedReader.readLine() + "\n");
                    leads.setColor(0xa300ff);
                    leads.setTitle(values[3]);
                    System.out.println("Test");
                    leads.setDescription("Retail -" + values[6] + "\n" + "Resell -" + values[28]);
                    channel.sendMessageEmbeds(leads.build()).queue();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

