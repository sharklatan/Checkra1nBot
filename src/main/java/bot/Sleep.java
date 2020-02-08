package bot;

import java.util.concurrent.TimeUnit;

import twitter4j.TwitterException;

/**
 * Pauses the execution of the thread.
 */
public class Sleep {
    public static void sleep(TwitterException exception) {
        try {
			TimeUnit.SECONDS.sleep(exception.getRateLimitStatus().getSecondsUntilReset());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public static void sleep(int seconds) {
        try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}