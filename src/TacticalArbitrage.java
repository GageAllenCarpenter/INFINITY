import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TacticalArbitrage extends ListenerAdapter {

    //Regex
    Matcher matcher;
    //Columns in CSV
    int i = 54;
    //Path of CSV
    String path ="src/Tactical Arbitrage CSVs/tacticalarbitrage_1648431828.csv";
    String line ="";
    //Objects needed to read and post the CSV
    EmbedBuilder leads = new EmbedBuilder();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    String[] values = new String[i];
    //Objects needed to post images
    String title = "foo.jpg";
    String defaultUrl = "https://media.discordapp.net/attachments/945046449484361731/957307011182252113/unknown.png?width=1008&height=671";
    //Objects to avoid duplicate posts


    /**
     * Constructor for objects of class Tactical Arbitrage
     * @throws FileNotFoundException
     */
    public TacticalArbitrage() throws IOException {
    }

    /**
     * Reading the data from the CSV file and sending it to the discord server.
     * @param event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!Leads"))
        {
            MessageChannel channel = event.getChannel();
            try
            {
                //READ OVER COLUMN HEADERS
                bufferedReader.readLine();
                // READ THE ROWS WITH PRODUCT INFORMATION
                while ((line = bufferedReader.readLine()) != null) {
                    Matcher matcher = Pattern.compile("(?<=^|,)[^,\"]*?(?=,|$)|(?<=(?:^|,)\").*?(?=\"(?:,|$))").matcher(line);
                    for (i = 0; i < values.length; i++) {
                        if (matcher.find()) {
                            values[i] = matcher.group();
                        }
                    }
                    System.out.println(values[11]);
                    //COMPARE THE PRODUCT TITLES AND REMOVE DUPLICATES
                    Set<String> productTitles = new HashSet<String>();
                    productTitles.add(values[11]);
                    if (productTitles.equals(productTitles))
                    {
                        bufferedReader.readLine();
                    }
                    //BUILD & FORMAT EMBEDS
                    leads.setColor(0xa300ff);
                    leads.setTitle(values[3]);
                    leads.setDescription("Retail -" + values[6] + "\n" + "Resell -" + values[28] + "\n" + "\n" + "Gated: ");
                    leads.setImage("attachment://" + title);
                    String urlText = values[16];
                    if (urlText.isEmpty()) {
                        urlText = defaultUrl;
                    }

                    InputStream imageStream = new URL(urlText).openStream();
                    leads.setThumbnail(event.getGuild().getIconUrl());
                    //Send the data to the server
                    channel.sendMessageEmbeds(leads.build()).addFile(imageStream, title).queue();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the unique Selling Applications for each asin
     * @return String
     */
    public String sellingApplication()
    {
        return "https://sellercentral.amazon.com/hz/approvalrequest/restrictions/approve?asin=" + values[21];
    }


}

