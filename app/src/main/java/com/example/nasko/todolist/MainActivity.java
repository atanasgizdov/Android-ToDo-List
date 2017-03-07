package com.example.nasko.todolist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // initialize variables for use cross-activity

    private EditText editText;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    // initialize UI elements and data structure
        editText = (EditText) findViewById(R.id.userEntry);
        btn = (Button) findViewById(R.id.addBtn);
        list = (ListView) findViewById(R.id.dataList);
        arrayList = new ArrayList<String>();

        // create adapter to translate data to UI
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        // Set data
        list.setAdapter(adapter);

        arrayList.add("Did this work?");
        arrayList.add("Did this work?");
        arrayList.add("Did this work?");
        adapter.notifyDataSetChanged();

        //handle insertion to list
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // grab entry and add to list
                arrayList.add(editText.getText().toString());
                // notify UI of change
                adapter.notifyDataSetChanged();
            }
        });

        //test data


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
