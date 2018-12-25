package com.chabo.tfrrsmobile;

import android.app.Activity;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class tfrrsSearch extends Activity{

    static class RetrieveFeedTask extends AsyncTask<String, Void, Elements> {

        private Exception exception;

        protected Elements doInBackground(String... inputText) {
            try {
                Document doc = Jsoup.connect("https://www.tfrrs.org/site_search.html").data(inputText[0], inputText[1]).userAgent("Mozilla").post(); //inputText[0] = key, 1 = value
                return doc.select("a[href*=results/]"); //filter to all results links only
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }
    }

}
