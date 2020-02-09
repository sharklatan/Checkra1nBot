package main;

import java.io.IOException;

import bot.Scraper;

class AppTest {
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        for (int i = 0; i < 10; i++) scraper.scrape();
    }
}