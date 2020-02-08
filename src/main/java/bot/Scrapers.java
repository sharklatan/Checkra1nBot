package bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Contains the web scraper function of the bot.
 */
public class Scrapers {
    /**
     * Scrapes The Verge.
     * 
     * @return A list of URLs collected by the scraper
     */
    public List<String> scrapeTheVerge() throws IOException {
        Document document = Jsoup.connect("https://www.google.com/search?q=jailbreak%20site%3Atheverge.com").get();
        Elements a = document.select("div.r a");
        List<String> urls = new ArrayList<>();
        for (Element element : a) {
            String url = element.attr("href");
            if (url.equals("#")) {
                continue;
            }
            else {
                urls.add(url);
            }
        }
        return urls;
    }

    /**
     * Scrapes r/Jailbreak on reddit.
     * 
     * @return A list of URLs collected by the scraper
     */
    public List<String> scrapeRedditPosts() throws IOException {
        Document document = Jsoup.connect("https://reddit.com/r/jailbreak").get();
        Elements a = document.getElementsByAttribute("data-click-id");
        List<String> urls = new ArrayList<>();
        for (Element element : a) {
            String url = element.attr("href");
            if (url == "") {
                continue;
            } 
            if (url.startsWith("https://www.reddit.com/user")) {
                continue;
            }
            if (url.startsWith("/user")) {
                continue;
            }
            else {
                urls.add(url);
            }
        }
        return urls;
    }
}