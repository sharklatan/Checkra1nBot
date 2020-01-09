package bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Contains the methods the bot can use to perform tasks.
 */
public class Actions {

    /**
     * Searches for tweets via a search key passed into the method.
     * Checks the quality of tweets too and makes sure the tweet meets a certain requirement.
     * @return Status object containing the search result
     * @throws TwitterException
     */
    public Status search(String searchKey) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(searchKey).lang("en").count(100);
        QueryResult result = twitter.search(query);
        List<Status> tweetList = result.getTweets();

        // Tweet list filtering
        class TweetProcessor {
            List<Status> statusList = new ArrayList<>();

            TweetProcessor(List<Status> tList) {
                for (Status t : tList) {
                    processTweet(t);
                }
            }
            
            void processTweet(Status status) {
                // Status must fulfil the following requirements
                if (
                    !Data.blockedAccounts.contains(status.getUser().getScreenName().toLowerCase()) &&
                    !Data.blockedWords.contains(status.getText().toLowerCase()) &&
                    status.getFavoriteCount() >= 5
                ) {
                    statusList.add(status);
                }
            }

            Status getTweet() {
                return (statusList.get(new Random().nextInt(statusList.size())));
            }
        }

        TweetProcessor tweetProcessor = new TweetProcessor(tweetList);

        return tweetProcessor.getTweet();
    }

    /**
     * Retweets a Status object passed into the method.
     * @return Retweeted status
     */
    public Status retweetStatus(Status status) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        long statusId = status.getId();
        Status retweetedStatus = twitter.retweetStatus(statusId);
        return retweetedStatus;
    }

    public int getRateLimitStatus(String endpoint) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        String family = endpoint.split("/", 3)[1];
        RateLimitStatus status = twitter.getRateLimitStatus(family).get(endpoint);
        return status.getResetTimeInSeconds();
    }
}