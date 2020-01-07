package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bot.Actions;
import bot.Data;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * The main class of the twitter bot.
 */
class App {
    public static void main(String[] args) throws TwitterException, InterruptedException {
        while (true) {
            try {
                Actions twitterActions = new Actions();
                Status status = twitterActions
                        .search(Data.searchKeys.get(new Random().nextInt(Data.searchKeys.size())));
                Status retweetedStatus = twitterActions.retweetStatus(status);
                consoleLog(String.format("Retweeted:\n\n%s\n\nby @%s", retweetedStatus.getText(),
                        retweetedStatus.getUser().getScreenName()));
            } catch (TwitterException exception) {
                consoleLog(exception.toString());
            } catch (IllegalArgumentException illegalArgException) {
                consoleLog("Results did not contain desired results");
                continue;
            }
            consoleLog("Sleeping");
            sleep();
        }
    }

    /**
     * Modified println method for logging.
     */
    static void consoleLog(String out) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(format.format(date) + " - " + out);
    }

    /**
     * To mitigate twitter rate limits.
     */
    static void sleep() throws InterruptedException {
        TimeUnit.MINUTES.sleep(new Random().nextInt(15) + 5);
    }
}