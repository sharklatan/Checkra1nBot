package main;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bot.Actions;
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
                        .search(Actions.getSearchKeys().get(new Random().nextInt(Actions.getSearchKeys().size())));
                twitterActions.retweetStatus(status);
                Actions.consoleLog("Retweeted: " + urlGenerator(status));
            } catch (TwitterException exception) {
                if (exception.getErrorCode() == 136) {
                    Actions.consoleLog("Blocked from retweeting user's tweets");
                    sleep(1);
                    continue;
                }
                if (exception.getErrorCode() == 327) {
                    Actions.consoleLog("Already retweeted");
                    sleep(1);
                    continue;
                }
                if (exception.getErrorCode() == 88) {
                    Actions.consoleLog("Rate limit exceeded");
                    Actions.consoleLog("Time until rate limit is over: " + exception.getRateLimitStatus().getSecondsUntilReset());
                    sleep(exception);
                    continue;
                }
                else {
                    Actions.consoleLog(exception.toString());
                }
            } catch (IllegalArgumentException exception) {
                Actions.consoleLog("Results did not contain desired results");
                continue;
            }
            sleep();
        }
    }

    static String urlGenerator(Status tweet) {
        String userName = tweet.getUser().getScreenName();
        Long tweetId = tweet.getId();
        return String.format("https://twitter.com/%s/status/%d", userName, tweetId);
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