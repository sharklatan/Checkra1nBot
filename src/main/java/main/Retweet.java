package main;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import bot.Actions;
import bot.Sleep;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * A thread which retweets tweets.
 */
class Retweet implements Runnable {
    /**
     * Main method of the class.
     */
    public void run() {
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
                    Sleep.sleep(15);
                    continue;
                }
                if (exception.getErrorCode() == 327) {
                    Actions.consoleLog("Already retweeted");
                    Sleep.sleep(15);
                    continue;
                }
                if (exception.getErrorCode() == 88) {
                    Actions.consoleLog("Rate limit exceeded");
                    Actions.consoleLog("Time until rate limit is over: " + exception.getRateLimitStatus().getSecondsUntilReset());
                    Sleep.sleep(exception);
                    continue;
                }
                else {
                    Actions.consoleLog(exception.toString());
                }
            } catch (IllegalArgumentException exception) {
                Actions.consoleLog("Results did not contain desired results");
                continue;
            } catch (IOException e) {
				e.printStackTrace();
			}
            sleep();
        }
    }

    /**
     * Generates a url from a tweet.
     * 
     * @return Tweet URL
     */
    static String urlGenerator(Status tweet) {
        String userName = tweet.getUser().getScreenName();
        Long tweetId = tweet.getId();
        return String.format("https://twitter.com/%s/status/%d", userName, tweetId);
    }
    
    /**
     * To mitigate Twitter rate limits.
     */
    static void sleep() {
        try {
			TimeUnit.MINUTES.sleep(new Random().nextInt(30) + 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}