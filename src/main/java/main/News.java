package main;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bot.Actions;
import bot.Scraper;
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
                Scraper scraper = new Scraper();

                String url = scraper.scrape();
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
     * To prevent over posting.
     */
    static void sleep() {
        try {
			TimeUnit.MINUTES.sleep(new Random().nextInt(90) + 30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}