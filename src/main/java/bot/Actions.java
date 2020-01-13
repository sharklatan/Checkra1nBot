package bot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Contains the methods the bot can use to perform tasks.
 */
public class Actions {

    /**
     * Searches for tweets via a search key passed into the method. Checks the
     * quality of tweets too and makes sure the tweet meets a certain requirement.
     * 
     * @return Status object containing the search result
     * @throws TwitterException
     * @throws IOException
     */
    public Status search(String searchKey) throws TwitterException, IOException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(searchKey).lang("en").count(100);
        QueryResult result = twitter.search(query);
        List<Status> tweetList = result.getTweets();

        // Tweet list filtering
        class TweetProcessor {
            List<Status> statusList = new ArrayList<>();

            TweetProcessor(List<Status> tList) throws IOException {
                for (Status t : tList) {
                    processTweet(t);
                }
            }

            void processTweet(Status status) throws IOException {
                // FIXME: Fix bug where blocked words array gets ignored
                // Status must fulfil the following requirements
                String[] statusSplitList = status.getText().toLowerCase().split(" ");
                List<String> statusSplit = new ArrayList<>();
                for (String i : statusSplitList) {
                    statusSplit.add(i);
                }

                if (
                    !Data.blockedAccounts.contains(status.getUser().getScreenName())
                        &&
                    !isSubset(Data.blockedWords, statusSplit)
                        &&
                    status.getFavoriteCount() >= 5
                ) {
                    consoleLog(
                        "\nBy @" + status.getUser().getScreenName()
                        + "\n" +
                        status.getText()
                    );
                    statusList.add(status);
                }
            }

            Status getTweet() {
                return (statusList.get(new Random().nextInt(statusList.size())));
            }

            boolean isSubset(List<String> parentList, List<String> childList) {
                int index = Collections.indexOfSubList(parentList, childList);
                if (index >= 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        TweetProcessor tweetProcessor = new TweetProcessor(tweetList);

        return tweetProcessor.getTweet();
    }

    /**
     * Retweets a Status object passed into the method.
     * 
     * @return Retweeted status
     */
    public Status retweetStatus(Status status) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        long statusId = status.getId();
        Status retweetedStatus = twitter.retweetStatus(statusId);
        return retweetedStatus;
    }

    /**
     * Modified println method for logging.
     */
    public static void consoleLog(String out) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(format.format(date) + " - " + out);
    }
}