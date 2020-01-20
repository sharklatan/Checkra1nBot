var Twit = require('twit');
var Twitter = new Twit(require('./config'));

/**
 * @param {string} string
 */
const consoleLog = string => {
  const date = new Date();
  console.log(`${date.getHours()}:${date.getMinutes()} - ${string}`);
}

const main = () => {
  const searchKey = () => {
    const searchKeys = require('./data').searchKeys;
    return searchKeys[Math.floor(Math.random() * searchKeys.length)];
  }
  Twitter.get(
    'search/tweets',
    { q: searchKey(), lang: 'en', count: 100 },
    (err, data, response) => {
      if (err) {
        consoleLog(err.message);
      } else {
        let tweetList = [];
        for (let i = 0; i < data.statuses.length; i++) {
          tweetList.push(data.statuses[i]);
        }
        let blockedWords = require('./data').blockedWords;
        let blockedAccounts = require('./data').blockedAccounts;
        let filteredList = tweetList.filter(tweet => {
          // Below is what we want in the tweet
          if (
            !blockedWords.every(blockedWord => tweet.text.toLowerCase().split(' ').includes(blockedWord))
            &&
            !blockedAccounts.includes(tweet.user.screen_name.toLowerCase())
            &&
            tweet.retweeted === false
            &&
            tweet.favorite_count >= 5
          ) {return true}
        });
        const finalTweet = filteredList[Math.floor(Math.random() * filteredList.length)];
        try {
          Twitter.post(
            'statuses/retweet/:id',
            { id: finalTweet.id_str },
            (err, data, response) => {
              if (err) {
                if (err.message.includes('retweeted')) {
                  consoleLog('Tweet has already been retweeted by me');
                  main();
                  return;
                } else if (err.message.includes('blocked')) {
                  consoleLog('Blocked from retweeting this user\'s tweets');
                  main();
                  return;
                }
                else {
                  consoleLog(err.message);
                }
              } else {
                consoleLog(`Retweeted`);
              }
            }
          );
        } catch (err) {
          consoleLog(err);
          main();
          return;
        }
      }
    }
  );
}

main();
setInterval(main, ((Math.floor(Math.random() * 20) + 10) * 1000) * 60);