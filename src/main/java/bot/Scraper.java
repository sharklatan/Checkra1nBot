package bot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Contains the web scraper function of the bot.
 */
public class Scraper {
    /**
     * Main scraper method.
     * 
     * @return A URL collected by the Scrapers class
     * @throws IOException
     */
    public String scrape() throws IOException {
        int[] scraperIndex = new int[3];
        int selectedIndex = new Random().nextInt(scraperIndex.length);
        switch (selectedIndex) {
            case 0: {
                List<String> urls = scrapeRedditPosts();
                return urls.get(new Random().nextInt(urls.size()));
            }
            case 1: {
                List<String> urls = scrapeTheVerge();
                return urls.get(new Random().nextInt(urls.size()));
            }
            case 2: {
                List<String> urls = scrapeIDownloadBlog();
                return urls.get(new Random().nextInt(urls.size()));
            }
        }
        return "";
    }

    /**
     * Logger method for outputting the list of URLs into a log file.
     * 
     * @throws IOException
     */
    private static void logUrls(List<String> urls) throws IOException {
        File file = new File("urls.log");
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(urls + "\n\n");
        fileWriter.close();
    }

    // Scraping methods

    /**
     * Scrapes The Verge.
     * 
     * @return A list of URLs collected by the scraper
     */
    static List<String> scrapeTheVerge() throws IOException {
        Document document = Jsoup.connect("https://www.google.com/search?q=jailbreak%20site%3Atheverge.com").get();
        Elements a = document.select("div.r a");
        List<String> urls = new ArrayList<>();
        for (Element element : a) {
            String url = element.attr("href");
            if (url.equals("#")) continue;
            if (url.startsWith("https://webcache.googleusercontent.com")) {
                String[] newUrls = url.split(":");
                String newUrl = newUrls[4];
                if (newUrl.startsWith("//")) {
                    urls.add("https:" + newUrl);
                }
                else {
                    urls.add("https://" + newUrl);
                }
            }
            else urls.add(url);
        }
        logUrls(urls);
        return urls;
    }

    /**
     * Scrapes r/Jailbreak on reddit.
     * 
     * @return A list of URLs collected by the scraper
     */
    static List<String> scrapeRedditPosts() throws IOException {
        Document document = Jsoup.connect("https://reddit.com/r/jailbreak").get();
        Elements a = document.getElementsByAttribute("data-click-id");
        List<String> urls = new ArrayList<>();
        for (Element element : a) {
            String url = element.attr("href");
            if (url == "") continue;
            if (url.startsWith("https://www.reddit.com/user")) continue;
            if (url.startsWith("/user")) continue;
            if (url.startsWith("/r/")) {
                String newUrl = "https://reddit.com" + url;
                urls.add(newUrl);
            }
            else urls.add(url);
        }
        logUrls(urls);
        return urls;
    }

    /**
     * Scrapes iDownloadBlog.
     * 
     * @return A list of URLs collected by the scraper
     */
    static List<String> scrapeIDownloadBlog() throws IOException {
        Document document = Jsoup.connect("https://www.idownloadblog.com/tag/jailbreak/").get();
        Elements a = document.select("a");
        List<String> urls = new ArrayList<>();
        for (Element element : a) {
            String url = element.attr("href");
            if (url == "") continue;
            if (url.startsWith("#")) continue;
            if (url.startsWith("/")) continue;
            if (url.startsWith("https://www.facebook.com/iDownloadBlog")) continue;
            if (url.startsWith("https://twitter.com/iDownloadBlog")) continue;
            if (url.startsWith("https://eepurl.com")) continue;
            if (url.startsWith("https://www.instagram.com/idownloadblog/")) continue;
            if (url.startsWith("https://youtube.com/idb/")) continue;
            if (url.startsWith("https://deals.idownloadblog.com")) continue;
            if (url.startsWith("https://geo.itunes.apple.com/us/app/idb-app")) continue;
            if (url == "https://www.idownloadblog.com/") continue;
            else urls.add(url);
        }
        logUrls(urls);
        return urls;
    }
}