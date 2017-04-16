package com.listcountry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText nameTxtVW;
    private EditText phoneTxtVW;
    private ListView lstView;
    private ListAdapter listAdaptor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button saveButton = (Button) findViewById(R.id.add);
        nameTxtVW = (EditText) findViewById(R.id.edtName);
        phoneTxtVW = (EditText) findViewById(R.id.edtPhone);
        if(database == null) {
            database = getDatabase("sampledb.txt");
            if(database.findEditTable("test") != null
                    && database.findEditTable("test").equals(""))
                database.execSQL("create table test (name varchar(100),phone varchar(50))");
        }
        lstView = (ListView) findViewById(R.id.lst);
        listAdaptor = lstView.getAdapter();

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(database != null)
                    database.execSQL("insert into test values(?,?)",
                            new String[]{nameTxtVW.getText().toString(),
                                    phoneTxtVW.getText().toString()});
                refreshList();
            }
        });
        Button removeButton = (Button) findViewById(R.id.remove);
        removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(database != null)
                    database.execSQL("delete from test where name = ? and phone= ?",
                            new String[]{nameTxtVW.getText().toString(),
                                    phoneTxtVW.getText().toString()});
                refreshList();
            }

        });



    }
    private void refreshList() {
        if(database != null) {
            Cursor cursor = database.query("test", new String[]{"name", "phone"},null,null,null,null,null);
            cursor.moveToFirst();
            ArrayAdapter listAdaptor = new ArrayAdapter(getApplicationContext(),
                    R.layout.text_view);
            while(cursor.moveToNext()) {
                listAdaptor.add(cursor.getString(0)+" - "+cursor.getString(1));
            }
            lstView.setAdapter(listAdaptor);
        }

    }

    private SQLiteDatabase getDatabase(String fileName) {
        return SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() +"/Android/data/com.example.sqliteexample/files/"+fileName,
                null);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
