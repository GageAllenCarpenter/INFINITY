import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads the CSV
 * @Author
 * @Version 2
 */
public class ProductReader {

    protected int columns = 54;
    final String path = "CSV/Leads.csv";
    final String productAttachment = "INFINITY.jpg";
    final String historicalDataAttachment = "HISTORY.jpg";
    final String backupAttachment = "https://media.discordapp.net/attachments/945046449484361731/957307011182252113/unknown.png?width=1008&height=671";
    private final Queue<EventData> eventDataQueue  = new ArrayDeque<>();
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    /**
     * Constructor for objects of class ProductReader
     *
     * @throws FileNotFoundException
     */
    public ProductReader() throws FileNotFoundException {
        service.scheduleAtFixedRate(this::processEventQueue, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * This method is called if you would like the application to read a CSV from Tactical Arbitrage
     *
     * @throws FileNotFoundException
     */
    public void readTacticalArbitrageCSV(MessageReceivedEvent event) throws FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            //READ OVER COLUMN HEADERS
            reader.readLine();
            // READ THE ROWS WITH PRODUCT INFORMATION
            String line = "";
            while ((line = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile("(?<=^|,)[^,\"]*?(?=,|$)|(?<=(?:^|,)\").*?(?=\"(?:,|$))").matcher(line);
                String[] column = new String[columns];
                for (columns = 0; columns < column.length; columns++) {
                    if (matcher.find()) {
                        column[columns] = matcher.group();
                    }
                }
                //ENSURE TITLES DO NOT DUPLICATE
                Set<String> Title = new HashSet<>();
                if(Title.equals(Title))
                {
                    reader.readLine();
                }
                //ENSURE ASINs DO NOT DUPLICATE
                Set<String> ASIN = new HashSet<>();
                ASIN.add(column[21]);
                if(ASIN.equals(ASIN))
                {
                    reader.readLine();
                }
                eventDataQueue.add(new EventData(event,column));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processEventQueue()  {
        EventData eventData = eventDataQueue.poll();
        if(eventData == null)
        {
            return;
        }
        try {
            productPost(eventData.event(), eventData.column());
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void productPost(MessageReceivedEvent event, String[] column) throws IOException {
        EmbedBuilder products = new EmbedBuilder();
        MessageChannel productEmbed = event.getChannel();
        products.setColor(0xa300ff);
        products.setTitle(column[3]);
        products.setThumbnail("attachment://" + productAttachment);
        String productURL = column[16];
        if (productURL.isEmpty()) {
            productURL = backupAttachment;
        }
        InputStream productStream = new URL(productURL).openStream();
        products.addField("Retail", column[6],true);
        products.addField("Resell", column[27], true);
        products.addField("ASIN", column[21], false);
        products.addField("Gross Profit", column[32], true);
        products.addField("Gross ROI", column[33] , true);
        products.addField("Sales Analysis Chart :chart_with_upwards_trend:", monthlySales(column), false);
        products.setImage("attachment://" + historicalDataAttachment);
        String historicalDataURL = historicalData(column);
        if(historicalDataURL.isEmpty()){
            historicalDataURL = backupAttachment;
        }
        InputStream historicalDataStream = new URL(historicalDataURL).openStream();
        productEmbed.sendMessageEmbeds(products.build()).addFile(productStream, productAttachment).addFile(historicalDataStream, historicalDataAttachment).setActionRow(sendButton(column)).queue();
    }

    /**
     * A collection of buttons that send relevant links to the discord server after the embedded messages
     * RetailURL - the retail stores URL
     * AmazonURL - the location to create your product listing
     * Seller Application - Apply to sell this product on Amazon
     */
    private List<Button> sendButton(String[] column)
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

    public String monthlySales(String[] column)
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

    /**
     * Creates a Keepa Graph to display chart information
     */
    public String historicalData(String[] column)
    {
        return "https://api.keepa.com/graphimage?key=c8tr8ocup195dsf9sgrubtqntl0v304hv4ab5p2ntbni4bhksgrfohiag0dopq5v&domain=1&amazon=1&new=1&used=0&salesrank=1&bb=1&fba=1&fbm=1&ld=1&wd=0&range=180&width=1000&height=500&asin=" + column[21];
    }

    private record EventData(MessageReceivedEvent event, String[] column) {}
}