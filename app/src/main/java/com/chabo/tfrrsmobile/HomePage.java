package com.chabo.tfrrsmobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private Elements currLinks;
    private int queuePage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setContentView(R.layout.activity_home_page);
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                    return true;
                case R.id.navigation_search:
                    setContentView(R.layout.activity_search_page);
                    BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                    return true;
            }
            return false;
        }
    };

    public void onViewCreated(View view, Bundle savedInstanceState)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void meetSearch(View v){
        EditText meetName = (EditText)findViewById(R.id.meetText);
        String meetString = meetName.getText().toString();
        this.queuePage = R.layout.meet_search_page;
        String[] kvSearchInput = {"meet", meetString};
        try {
            this.currLinks = (new tfrrsSearch.RetrieveFeedTask().execute(kvSearchInput)).get();
        }catch(Exception e){
            System.out.println(e);
        }
        setContentView(this.queuePage);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TableLayout searchTable = (TableLayout)findViewById(R.id.searchTable);
        for(final Element meetEle: this.currLinks) {
            TableRow currTableRow = new TableRow(getBaseContext());
            TextView meetNameAndLink = new TextView(getBaseContext());
            meetNameAndLink.append(meetEle.text());
            meetNameAndLink.setClickable(true);
            meetNameAndLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get info for this meet
                    getMeet(meetEle);
                }
            });
            currTableRow.addView(meetNameAndLink);
            searchTable.addView(currTableRow);
        }

    }

    public void getMeet(Element meetEle){
        setContentView(R.layout.meet_info_page);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView meetTextView = (TextView) findViewById(R.id.meetNameTextView);
        meetTextView.setText(meetEle.text());
        String currLink = meetEle.attr("HREF");
        try {
            this.currLinks = (new tfrrsGetMeet.RetrieveFeedTask().execute(currLink)).get();
        }catch(Exception e){
            System.out.println(e);
        }
        TableLayout meetTable = (TableLayout)findViewById(R.id.meetTable);
        meetTable.setColumnStretchable(0,true);
        meetTable.setColumnStretchable(1,true);
        String meetInfoString = "";
        Boolean men = true;
        int compileNum = 0;   //once we get to 2 compiled element,s we start on the women
        int womenCount = 0;
        ArrayList<TableRow> eventRows = new ArrayList<>();
        TableRow startingTableRow = new TableRow(getBaseContext());
        TextView menText = new TextView(getBaseContext());
        menText.setText("MEN");
        menText.setTextSize(20);
        menText.setTextColor(getResources().getColor(R.color.colorPrimary));
        TextView womenText = new TextView(getBaseContext());
        womenText.setText("WOMEN");
        womenText.setTextSize(20);
        womenText.setTextColor(getResources().getColor(R.color.colorPrimary));
        startingTableRow.addView(menText);
        startingTableRow.addView(womenText);
        meetTable.addView(startingTableRow);
        for(final Element eventEle: this.currLinks) {
            if(eventEle.text().equals("Compiled")){
                if( compileNum > 0) {
                    men = false;
                }else{
                    compileNum++;
                }

            }
            if(eventEle.hasAttr("HREF") && eventEle.attr("HREF").contains("Women")){
                womenCount++;
            }
            if(eventEle.hasClass("panel-heading-normal-text inline-block")){
                meetInfoString = meetInfoString.concat(eventEle.text() + " | ");
            }else if(eventEle.hasClass("panel-heading-normal-text inline-block ")) {
                meetInfoString = meetInfoString.concat(eventEle.text());
                TextView meetInfo = (TextView) findViewById(R.id.meetInfoTextView);
                meetInfo.setText(meetInfoString);
            }else if( !eventEle.text().equals("Women") && !eventEle.text().equals("Men")){
                //need to split into columns: men/women
                TableRow currTableRow;
                if(men) {
                    currTableRow = new TableRow(getBaseContext());
                    eventRows.add(currTableRow);
                }else{
                    currTableRow = eventRows.get(womenCount);
                }
                TextView eventLink = new TextView(getBaseContext());
                eventLink.append(eventEle.text());
                eventLink.setClickable(true);
                eventLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get info for this event
                        getEvent(eventEle);
                    }
                });
                currTableRow.addView(eventLink);
                if(!men) {
                    meetTable.addView(currTableRow);
                }
            }
        }
    }

    //this one will probably be the hardest, formatting tables for events might be difficult
    //gotta link athletes, times don't have to have links
    public void getEvent(Element ele){
        
    }

    public void teamSearch(View v){

    }

    public void athleteSearch(View v) {

    }

}
