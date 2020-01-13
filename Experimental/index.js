var Twit = require('twit');
var Twitter = new Twit(require('./config'));

function search(callback) {
  function searchKey() {
    const searchKeys = require('./data').searchKeys;
    const searchKey = searchKeys[Math.random(searchKeys.length)];
    return searchKey;
  }
  Twitter.get(
    'search/tweets',
    { q: , lang: 'en', count: 100 },
    (err, data, response) => {
      console.log(data);
    }
  );
}

search()