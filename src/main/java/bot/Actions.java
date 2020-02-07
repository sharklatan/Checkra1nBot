package bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;

/**
 * Contains the methods the bot can use to perform tasks.
 */
public class Actions {

    // Global variables
    private static String blockedWordsFileName = "blocked-words.txt";
    private static String blockedAccountsFileName = "blocked-accounts.txt";
    private static String searchKeysFileName = "search-keys.txt";

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

            boolean retweetedByMe(Status status) {
                String json = TwitterObjectFactory.getRawJSON(status);
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getBoolean("retweeted");
            }

            void logTweet(Status status) throws IOException {
                String json = TwitterObjectFactory.getRawJSON(status);
                JSONObject jsonObject = new JSONObject(json);
                File file = new File("tweets.log");
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write("Tweet JSON:\n" + jsonObject.toString(4) + "\n\n");
                fileWriter.close();
            }

            void processTweet(Status status) throws IOException {
                String[] statusSplitList = status.getText().toLowerCase().split(" ");
                List<String> statusSplit = new ArrayList<>();
                for (String i : statusSplitList) {
                    statusSplit.add(i);
                }

                // Status must fulfil the following requirements
                if (
                    !getBlockedAccounts().contains(status.getUser().getScreenName().toLowerCase())
                    &&
                    !isSubset(getBlockedWords(), statusSplit)
                    &&
                    status.getFavoriteCount() >= 5
                    &&
                    !retweetedByMe(status)
                ) {
                    statusList.add(status);
                    logTweet(status);
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

    // Blocked Words

    /**
     * Adds a word to the file containing the blocked words.
     * 
     * @throws IOException
     */
    public static void addBlockedWord(String word) throws IOException {
        File file = new File(blockedWordsFileName);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(word + "\n");
        fileWriter.close();
    }

    /**
     * Returns an array of all the blocked words.
     * 
     * @throws IOException
     */
    public static List<String> getBlockedWords() throws IOException {
        File file = new File(blockedWordsFileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> blockedWords = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            blockedWords.add(line);
        }
        reader.close();
        return blockedWords;
    }

    // Blocked Accounts

    /**
     * Adds a word to the file containing the blocked accounts.
     * 
     * @throws IOException
     */
    public static void addBlockedAccount(String word) throws IOException {
        File file = new File(blockedAccountsFileName);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(word + "\n");
        fileWriter.close();
    }

    /**
     * Returns an array of all the blocked accounts.
     * 
     * @throws IOException
     */
    public static List<String> getBlockedAccounts() throws IOException {
        File file = new File(blockedAccountsFileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> blockedAccounts = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            blockedAccounts.add(line);
        }
        reader.close();
        return blockedAccounts;
    }

    // Search Keys

    /**
     * Adds a word to the file containing the search keys.
     * 
     * @throws IOException
     */
    public static void addSearchKey(String word) throws IOException {
        File file = new File(searchKeysFileName);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(word + "\n");
        fileWriter.close();
    }

    /**
     * Returns an array of all the search keys.
     * 
     * @throws IOException
     */
    public static List<String> getSearchKeys() throws IOException {
        File file = new File(searchKeysFileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> searchKeys = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            searchKeys.add(line);
        }
        reader.close();
        return searchKeys;
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