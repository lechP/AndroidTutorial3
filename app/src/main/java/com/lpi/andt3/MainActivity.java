package com.lpi.andt3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    private SharedPreferences sharedPreferences;


    private TextView mainTextView;
    private Button mainButton;
    private EditText mainEditText;
    private ListView mainListView;
    private ArrayAdapter<String> arrayAdapter;
    private ShareActionProvider shareActionProvider;
    private ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = (TextView) findViewById(R.id.main_textview);

        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        mainEditText = (EditText) findViewById(R.id.main_edittext);
        mainListView = (ListView) findViewById(R.id.main_listview);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        mainListView.setAdapter(arrayAdapter);
        mainListView.setOnItemClickListener(this);

        displayWelcome();
    }

    private void displayWelcome() {
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = sharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {
            Toast.makeText(this, "welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("what is your name?");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputName = input.getText().toString();
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.apply();

                    Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!", Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alert.show();
        }
    }

    @Override
    public void onClick(View v) {
        String currentEditText = mainEditText.getText().toString();
        mainTextView.setText(currentEditText + " is learning Android development!");
        names.add(currentEditText);
        setShareIntent();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("omg android", position + ": " + names.get(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        if (shareItem != null) {
            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        setShareIntent();
        return super.onCreateOptionsMenu(menu);
    }

    private void setShareIntent() {
        if (shareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
