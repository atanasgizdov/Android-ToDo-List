package com.example.nasko.todolist;
import com.example.nasko.todolist.ListItem;
import com.example.nasko.todolist.DatabaseHandler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

public class MainActivity extends AppCompatActivity {

    // initialize variables for use cross-activity

    private EditText editText;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private List<ListItem> listitem;
    private Integer currentlyselectedindex;
    private String currentlyselectedvalue;
    private TextToSpeech mtts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // initialize text to speech, check status and set locale

        initializeTTS();

        // initialize UI elements and data structure
        editText = (EditText) findViewById(R.id.userEntry);
        btn = (Button) findViewById(R.id.addBtn);
        list = (ListView) findViewById(R.id.dataList);
        arrayList = new ArrayList<String>();

        // create adapter to translate data to UI
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        // initialize DB

        DatabaseHandler db = new DatabaseHandler(this);

        // Set data
        list.setAdapter(adapter);
        list.setChoiceMode(1);


        //test data for testing purposes
        //arrayList.add("Did this work?");
        //adapter.notifyDataSetChanged();


        //delete all records for testing purposes
        //db.deleteAllContacts();

        //test data for testing purposes
        /* Log.d("Insert: ", "Inserting ..");
        db.addRecord(new ListItem("Test1"));
        db.addRecord(new ListItem("Test"));
        db.addRecord(new ListItem("Atanas")); */


        // Reading all contacts to display on start up
        Log.d("Reading: ", "Reading all contacts..");
        listitem = db.getAllContacts();

        // loop through and write to log to display table data
        for (ListItem cn : listitem) {

            String log = "Id: "+cn.getID()+" ,Name: " + cn.getListItem();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        for (ListItem cn : listitem) {
            String entry = cn.getListItem();
            // grab entry and add to list
            arrayList.add(entry.toString());
            // notify UI of change
            adapter.notifyDataSetChanged();
        }

        //handle button insertion to list - "Add button"
        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                addRecord();
            }
        });

        // Create onclick activity
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {


                // send user to edit/delete box
                showInputBox(arrayList.get(position), position);

                // grab values and store globally in case person wants to use menu to edit

                currentlyselectedindex = position;
                currentlyselectedvalue = arrayList.get(position);



                //close keyboard


                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                // When clicked, populate textview for editing

                //editText.setText(((TextView) view).getText());


            }
        });
    }
    // initialize text to speech
    public void initializeTTS() {
        mtts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mtts.setLanguage(Locale.US);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry but text to speech failed - please check your device and try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void showInputBox(String oldItem, final int index){
            final Dialog dialog=new Dialog(MainActivity.this);
            dialog.setTitle("Input Box");
            dialog.setContentView(R.layout.edit_box);
            TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
            txtMessage.setText("Update item");
            txtMessage.setTextColor(Color.parseColor("#ff2222"));
            final EditText editText=(EditText)dialog.findViewById(R.id.txtinput);
            editText.setText(oldItem);

            Button bt=(Button)dialog.findViewById(R.id.btdone);
            Button bt2 =(Button)dialog.findViewById(R.id.btdelete);

            // edit button
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update entry in string array
                    arrayList.set(index,editText.getText().toString());
                    //grab object
                    ListItem item_edit = listitem.get(index);
                    //update object in object array
                    item_edit.setListItem(editText.getText().toString());
                    //update item in DB
                    editRecordFromDB(item_edit);
                    // log for DB debugging purposes
                    for (ListItem cn : listitem) {
                        String entry = cn.getListItem();
                        // grab entry and add to list
                        String log = "Id: " + item_edit.getID() + " ,Name: " + item_edit.getListItem();
                        // Writing Contacts to log
                        Log.d("Name: ", log);
                    }
                    //update view
                    adapter.notifyDataSetChanged();
                    // announce
                    mtts.speak("You have edited" + " " + editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                    dialog.dismiss();
                }
            });

            // Delete button
            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove from string array @ passed index
                    arrayList.remove(index);
                    //grab object
                    ListItem item_delete = listitem.get(index);
                    //delete from DB
                    deleteRecordFromDB(item_delete);
                    //delete from object array
                    listitem.remove(index);
                    // show rows for debugging purposes
                    for (ListItem cn : listitem) {
                        String entry = cn.getListItem();
                        // grab entry and add to list
                        String log = "Id: " + item_delete.getID() + " ,Name: " + item_delete.getListItem();
                        // Writing Contacts to log
                        Log.d("Name: ", log);
                    }
                    // announce
                    mtts.speak("You have deleted" + " " + editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }



    // add record
    public void addRecord (){
        // grab entry and add to array list of strings
        arrayList.add(editText.getText().toString());
        //create new object of type listitem
        ListItem item = new ListItem(editText.getText().toString());
        // add to object array so we can track equal object
        listitem.add(item);
        //add to DB
        DatabaseHandler db = new DatabaseHandler(this);
        db.addRecord(item);
        // notify UI of change
        adapter.notifyDataSetChanged();
}
    // method to edit from DB
    public void editRecordFromDB(ListItem listItem){
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateContact(listItem);
    }

    // method to delete from DB
    public void deleteRecordFromDB(ListItem listItem){
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteContact(listItem);
    }

    // menu buttons
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
        if (id == R.id.add_entry) {
            // grab entry and add to list
            arrayList.add(editText.getText().toString());
            // notify UI of change
            adapter.notifyDataSetChanged();
        }

        if (id == R.id.modify_entry) {
            if (currentlyselectedindex != null) {
                // grab entry from global and modify in list
                showInputBox(currentlyselectedvalue, currentlyselectedindex);

                // notify UI of change
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getApplicationContext(), "Please select a valid entry",
                        Toast.LENGTH_SHORT).show();
            }
    }

        if (id == R.id.delete_entry) {
            // grab entry and add to list
            if (currentlyselectedindex != null) {
                showInputBox(currentlyselectedvalue, currentlyselectedindex);
                // notify UI of change
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getApplicationContext(), "Please select a valid entry",
                        Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.save_list) {
            return true;
        }

        if (id == R.id.close_app) {
            return true;
            //this.finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }
}
