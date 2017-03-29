package com.example.nasko.todolist;

/**
 * Created by Nasko on 3/28/2017
 * Used to create class with list objects and return/set variables
 */

public class ListItem {
    int _id;
    String _content;

    //Empty constructor

    public ListItem () {

    }

    // constructor
    public ListItem (int id, String content){
        this._id = id;
        this._content = content;
    }

    // constructor
    public ListItem (String content){
        this._content = content;
    }

    // get ID
    public int getID(){
        return this._id;
    }

    // set ID
    public void setID (int id){
        this._id = id;
    }

    // get content

    public String getListItem () {
        return this._content;
    }

    //set list content

    public void setListItem (String content) {
        this._content = content;
    }




}
