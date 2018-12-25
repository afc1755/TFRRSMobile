package com.chabo.tfrrsmobile;

import android.app.Activity;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class tfrrsGetMeet extends Activity{

    static class RetrieveFeedTask extends AsyncTask<String, Void, Elements> {

        private Exception exception;

        protected Elements doInBackground(String... inputText) {
            try {
                Document doc = Jsoup.connect("https:" + inputText[0]).get(); //inputText[0] = meet's url
                Elements eles = new Elements();
                eles.addAll(doc.getElementsByClass("panel-heading-normal-text inline-block"));//filter to get all the header info first
                eles.addAll(doc.getElementsByClass("panel-heading-normal-text inline-block "));//someone accidentally added a space
                eles.addAll(doc.select("a[href*=results/]"));
                return eles;
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }
    }

}
