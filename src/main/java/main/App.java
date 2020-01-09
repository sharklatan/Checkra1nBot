package main;

import java.io.IOException;
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
 * Github: https://github.com/raphtlw/Checkra1nBot
 */
class App {
    public static void main(String[] args) throws TwitterException, InterruptedException, IOException {
        while (true) {
            try {
                Actions twitterActions = new Actions();
                Status status = twitterActions
                        .search(Data.searchKeys.get(new Random().nextInt(Data.searchKeys.size())));
                Status retweetedStatus = twitterActions.retweetStatus(status);
                consoleLog(String.format("Retweeted:\n\n%s\n\nby @%s", retweetedStatus.getText(),
                        status.getUser().getScreenName()));
            } catch (TwitterException exception) {
                if (exception.getErrorCode() == 136) {
                    consoleLog("Blocked from retweeting user's tweets");
                    sleep(2);
                    continue;
                }
                if (exception.getErrorCode() == 327) {
                    consoleLog("Already retweeted");
                    sleep(2);
                    continue;
                }
                if (exception.getErrorCode() == 88) {
                    consoleLog("Rate limit exceeded");
                    consoleLog("Time until rate limit is over: " + exception.getRateLimitStatus().getSecondsUntilReset());
                    sleep(exception);
                    continue;
                }
                else {
                    consoleLog(exception.toString());
                }
            } catch (IllegalArgumentException exception) {
                consoleLog("Results did not contain desired results");
                continue;
            }
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
        TimeUnit.MINUTES.sleep(new Random().nextInt(30) + 10);
    }

    static void sleep(TwitterException exception) throws InterruptedException {
        TimeUnit.MINUTES.sleep(exception.getRateLimitStatus().getSecondsUntilReset());
    }

    static void sleep(int minutes) throws InterruptedException {
        TimeUnit.MINUTES.sleep(minutes);
    }
}