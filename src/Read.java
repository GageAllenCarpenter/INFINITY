import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the CSV
 * @Author Gage Carpenter
 * @Version 2
 */
public class Read {

    protected int row;
    protected int columns = 54;

    String path = "CSV/Leads.csv";
    String line = "";
    String[] column = new String[columns];
    String attachment = "INFINITY.jpg";
    String backupAttachment = "https://media.discordapp.net/attachments/945046449484361731/957307011182252113/unknown.png?width=1008&height=671";

    BufferedReader reader = new BufferedReader(new FileReader(path));

    private EmbedBuilder products = new EmbedBuilder();
    private EmbedBuilder historicalData = new EmbedBuilder();

    /**
     * Constructor for objects of class Read
     *
     * @throws FileNotFoundException
     */
    public Read() throws FileNotFoundException {
    }

    /**
     * This method is called if you would like the application to read a CSV from Tactical Arbitrage
     *
     * @throws FileNotFoundException
     */
    public void readTacticalArbitrageCSV(MessageReceivedEvent event) throws FileNotFoundException {
        try {
            //READ OVER COLUMN HEADERS
            reader.readLine();
            // READ THE ROWS WITH PRODUCT INFORMATION
            while ((line = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile("(?<=^|,)[^,\"]*?(?=,|$)|(?<=(?:^|,)\").*?(?=\"(?:,|$))").matcher(line);
                for (columns = 0; columns < column.length; columns++) {
                    if (matcher.find()) {
                        column[columns] = matcher.group();
                    }
                }
                productPost(event);
                products = new EmbedBuilder();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //POST SECTION

    public void productPost(MessageReceivedEvent event) throws IOException {
        MessageChannel productEmbed = event.getChannel();
        products.setColor(0xa300ff);
        products.setTitle(column[3]);
        products.setThumbnail("attachment://" + attachment);
        String productURL = column[16];
        if (productURL.isEmpty()) {
            productURL = backupAttachment;
        }
        InputStream imageStream = new URL(productURL).openStream();
        products.addField("Retail", column[6],true);
        products.addField("Resell", column[27], true);
        products.addField("ASIN", column[21], false);
        products.addField("Gross Profit", column[32], true);
        products.addField("Gross ROI", column[33] , true);
        products.addField("Sales Analysis Chart :chart_with_upwards_trend:", monthlySales(), false);
        productEmbed.sendMessageEmbeds(products.build()).addFile(imageStream, attachment).setActionRow(sendButton()).queue();
    }

    /**
     * A collection of buttons that send relevant links to the discord server after the embedded messages
     * RetailURL - the retail stores URL
     * AmazonURL - the location to create your product listing
     * Seller Application - Apply to sell this product on Amazon
     */
    private List<Button> sendButton()
    {
        String amazonURL = column[12];
        if (amazonURL.isEmpty()) {
            amazonURL = backupAttachment;
        }
        String retailURL = column [4];
        if (retailURL.isEmpty()) {
            retailURL = backupAttachment;
        }
        String sellerApplicationURL = "https://sellercentral.amazon.com/hz/approvalrequest/restrictions/approve?asin="+column[21];
        if (sellerApplicationURL.isEmpty()) {
            sellerApplicationURL = backupAttachment;
        }
        List <Button> buttons = new ArrayList<>();
        buttons.add(Button.link(retailURL,"Retail").withEmoji(Emoji.fromUnicode("\uD83D\uDED2")));
        buttons.add(Button.link(amazonURL,"Amazon").withEmoji(Emoji.fromUnicode("\uD83C\uDFA5")));
        buttons.add(Button.link(sellerApplicationURL,"Seller Application").withEmoji(Emoji.fromUnicode("\uD83D\uDCDC")));
        return buttons;
    }
    //ANALYSIS SECTION

    public String monthlySales()
    {
        try {
            if (Integer.parseInt(column[42]) > 100 && Integer.parseInt(column[42]) < 500) {
                return "Monthly Sales: " + column[42] + " :star:";
            } else if (Integer.parseInt(column[42]) > 500) {
                return "Monthly Sales: " + column[42] + " :rocket:";
            } else {
                return "Monthly Sales: " + column[42];
            }
        }
        catch(NumberFormatException n)
        {
            return column[42];
        }
    }
}
