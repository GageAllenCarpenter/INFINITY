
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class SandBox extends ListenerAdapter {

    //Columns in CSV
    int i = 54;
    //Path of CSV
    String path ="Tactical Arbitrage CSVs/Leads.csv";
    String line ="";
    //Objects needed to read and post the CSV
    EmbedBuilder leads = new EmbedBuilder();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    String[] values = new String[i];
    //Objects needed to post images
    String title = "foo.jpg";
    String defaultUrl = "https://media.discordapp.net/attachments/945046449484361731/957307011182252113/unknown.png?width=1008&height=671";
    //KEEPA
    String keepa = "keepa.png";
    EmbedBuilder keepas = new EmbedBuilder();
    EmbedBuilder validation = new EmbedBuilder();

    public SandBox() throws FileNotFoundException {
    }

    public void onMessageReceived(MessageReceivedEvent event) { //Good
        {
            //String[] args = event.getMessage().getContentRaw().split(" ");
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if (attachments.isEmpty())
            {
                return;
            }
            else
            {
                //SEND A MESSAGE SHOWING THAT THE FILE REGISTERED
                MessageChannel channel = event.getChannel();
                validation.setColor(0xa300ff);
                validation.setTitle("FILE HAS BEEN SAVED");
                //SAVE FILE TO TACTICAL ARBITRAGE CSVs
                int i = 0;
                attachments.get(0).downloadToFile("Tactical Arbitrage CSVs/" + "Leads" + ".csv");
            }
            MessageChannel channel = event.getChannel();
            try
            {
                //READ OVER COLUMN HEADERS
                bufferedReader.readLine();
                // READ THE ROWS WITH PRODUCT INFORMATION
                while ((line = bufferedReader.readLine()) != null)
                {
                    Matcher matcher = Pattern.compile("(?<=^|,)[^,\"]*?(?=,|$)|(?<=(?:^|,)\").*?(?=\"(?:,|$))").matcher(line);
                    for (i = 0; i < values.length; i++)
                    {
                        if (matcher.find())
                        {
                            values[i] = matcher.group();
                        }
                    }
                    //COMPARE THE PRODUCT TITLES AND REMOVE DUPLICATES
                    Set<String> productTitles = new HashSet<String>();
                    productTitles.add(values[11]);
                    if (productTitles.equals(productTitles))
                    {
                        bufferedReader.readLine();
                    }
                    //COMPARE THE ASINS AND REMOVE DUPLICATES
                    Set<String> ASIN = new HashSet<String>();
                    ASIN.add(values[21]);
                    if (ASIN.equals(ASIN))
                    {
                        bufferedReader.readLine();
                    }
                    //BUILD & FORMAT PRODUCT EMBEDS
                    leads.setColor(0xa300ff);
                    leads.setTitle(values[3]);
                    leads.addField("Retail",values[6],true);
                    leads.addField("Resell", values[28], true);
                    leads.addField("ASIN", values[21], false);
                    leads.addField("Gross Profit", values[32], true);
                    leads.addField("Gross ROI", values[33] , true);

                    //SET PRODUCT EMBED THUMBNAIL IMAGE
                    leads.setThumbnail("attachment://" + title);
                    String productURL = values[16];
                    if (productURL.isEmpty()) {
                        productURL = defaultUrl;
                    }
                    InputStream imageStream = new URL(productURL).openStream();
                    //SEND PRODUCT LEAD EMBED TO SERVER
                    channel.sendMessageEmbeds(leads.build()).addFile(imageStream, title).queue();

                    //BUILD & FORMAT KEEPA EMBEDS
                    keepas.setColor(0xa300ff);
                    keepas.setDescription("Sales Analysis Chart :chart_with_upwards_trend:"
                            + "\n" + monthlySales());
                    keepas.setImage("attachment://" + keepa);
                    String keepaURL = keepaTracker();
                    if (keepaURL.isEmpty()){
                        keepaURL = defaultUrl;
                    }
                    InputStream keepaStream = new URL(keepaURL).openStream();
                    //SEND KEEPA LEAD EMBED TO SERVER
                    channel.sendMessageEmbeds(keepas.build()).addFile(keepaStream,keepa).setActionRow(sendButton()).queue();
                    leads = new EmbedBuilder();
                    keepas = new EmbedBuilder();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A collection of buttons that send relevant links to the discord server after the embedded messages
     * RetailURL - the retail stores URL
     * AmazonURL - the location to create your product listing
     * Seller Application - Apply to sell this product on Amazon
     */
    private List<Button> sendButton()
    {
        String amazonURL = values[12];
        if (amazonURL.isEmpty()) {
            amazonURL = defaultUrl;
        }
        String retailURL = values [4];
        if (retailURL.isEmpty()) {
            retailURL = defaultUrl;
        }
        String sellerApplicationURL = "https://sellercentral.amazon.com/hz/approvalrequest/restrictions/approve?asin="+values[21];
        if (sellerApplicationURL.isEmpty()) {
            sellerApplicationURL = defaultUrl;
        }
        List <Button> buttons = new ArrayList<>();
        buttons.add(Button.link(retailURL,"Retail").withEmoji(Emoji.fromUnicode("\uD83D\uDED2")));
        buttons.add(Button.link(amazonURL,"Amazon").withEmoji(Emoji.fromUnicode("\uD83C\uDFA5")));
        buttons.add(Button.link(sellerApplicationURL,"Seller Application").withEmoji(Emoji.fromUnicode("\uD83D\uDCDC")));

        return buttons;
    }

    /**
     * Creates a Keepa Graph to display chart information
     */
    public String keepaTracker()
    {
        return "https://api.keepa.com/graphimage?key=8nik2s5ivufk1glm0blf522hu1h7j2qb9vp1non4iggltkkvtvtfjt3pbgto7cvn&domain=1&amazon=1&new=1&used=0&salesrank=1&bb=1&fba=1&fbm=1&ld=1&wd=0&range=180&width=1000&height=500&asin=" + values[21];
    }

    /**
     * Monthly Sales selector that specifies if a product has enough sales to receive an emoji
     * Golden Star for Sales over 100 but less than 500
     * Rocket ship for Sales over 500
     */
    public String monthlySales()
    {
        if ( Integer.parseInt(values[42]) > 100 && Integer.parseInt(values[42]) < 500)
        {
            return "Monthly Sales: " + values[42] + " :star:";
        }
        else if (Integer.parseInt(values[42]) > 500)
        {
            return "Monthly Sales: " + values[42] + " :rocket:";
        }
        else
        {
            return "Monthly Sales: " + values[42];
        }
    }
}


