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
        for(final Element ele: this.currLinks) {
            TableRow currTableRow = new TableRow(getBaseContext());
            TextView meetNameAndLink = new TextView(getBaseContext());
            meetNameAndLink.append(ele.text());
            meetNameAndLink.setTextSize(18);
            meetNameAndLink.setClickable(true);
            meetNameAndLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do stuff for this meet
                    getMeet(ele);
                }
            });
            currTableRow.addView(meetNameAndLink);
            searchTable.addView(currTableRow);
        }

    }

    public void getMeet(Element ele){
        setContentView(R.layout.meet_info_page);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TextView meetTextView = (TextView) findViewById(R.id.meetNameTextView);
        meetTextView.setText(ele.text());
        String currLink = ele.attr("HREF");
        try {
            this.currLinks = (new tfrrsGetMeet.RetrieveFeedTask().execute(currLink)).get();
        }catch(Exception e){
            System.out.println(e);
        }

    }

    public void teamSearch(View v){

    }

    public void athleteSearch(View v) {

    }

}
