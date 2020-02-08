package main;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bot.Actions;
import bot.Scrapers;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * A thread which scrapes news about Checkra1n stuff and tweets it.
 */
class News implements Runnable {
    /**
     * Main method of the class.
     */
    public void run() {
        while (true) {
            try {
                Actions actions = new Actions();
    
                String url = scrape();
                Status tweeted = actions.postTweet(url);
                Actions.consoleLog(tweeted.getText());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            sleep();
        }
    }

    /**
     * All the scraping is done here.
     * 
     * @return A URL collected by the Scrapers class
     * @throws IOException
     */
    public String scrape() throws IOException {
        Scrapers scrapers = new Scrapers();
        int[] scraperIndex = new int[2];
        int selectedIndex = new Random().nextInt(scraperIndex.length);
        switch (selectedIndex) {
            case 0: {
                List<String> urls = scrapers.scrapeRedditPosts();
                return urls.get(new Random().nextInt(urls.size()));
            }
            case 1: {
                List<String> urls = scrapers.scrapeTheVerge();
                return urls.get(new Random().nextInt(urls.size()));
            }
        }
        return "";
    }

    /**
     * To prevent over posting.
     */
    static void sleep() {
        try {
			TimeUnit.MINUTES.sleep(new Random().nextInt(60) + 30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}