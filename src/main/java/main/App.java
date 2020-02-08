package main;

/**
 * The main class of the twitter bot.
 * Github: https://github.com/raphtlw/Checkra1nBot
 */
class App {
    public static void main(String[] args) {
        Thread retweet = new Thread(new Retweet());
        Thread news = new Thread(new News());
        retweet.start();
        news.start();
    }
}