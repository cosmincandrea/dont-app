package com.example.cosmin.dont;


import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import processing.core.PApplet;

public class ProductsList extends AppCompatActivity {
    AutoCompleteTextView autocomplete;
    private DatabaseReference mdatabase;
    public static String name;
    public static String link1, link2;
    public static String produse;
    ArrayList<String> list = new ArrayList<String>();
    String[] deDus;
    int tot;
    String[] produsee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        name = intent.getStringExtra(MapsActivity.TITLE);
        if (name == null) {
            name = intent.getExtras().getString("nume");
        }

        setContentView(R.layout.activity_products_list);
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                link1 = dataSnapshot.child(name).child("Link_1").getValue().toString();
                link2 = dataSnapshot.child(name).child("Link_2").getValue().toString();
                faChestii(link1, link2);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("err");
            }
        });
        //final ListView listview = (ListView) findViewById(R.id.listvv);
        //final ArrayAdapter adapp = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        //listview.setAdapter(adapp);
        //listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@Override
            //public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //adapp.remove(adapp.getItem(i));
            //}
        //});

    }

    public void faChestii(String link1, String link2) {
        PApplet d;
        d = new PApplet();
        produsee = d.loadStrings(link2);
        final String[] produseCoord = new String[produsee.length];
        final int[] pret = new int[produsee.length];

        for (int i = 0; i < produsee.length; i++) {
            String[] gunoi;
            gunoi = d.split(produsee[i], ':');
            produsee[i] = gunoi[1];
            produseCoord[i] = gunoi[0];
//            pret[i] = PApplet.parseInt(gunoi[2]);
        }
        final String[] deDus = new String[produsee.length];
        tot = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, produsee);
        final ArrayAdapter adapp = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter);
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                list.add(selection);
                autocomplete.setText("");
                adapp.notifyDataSetChanged();

            }
        });
        final ListView listview = (ListView) findViewById(R.id.listvv);
        //final ArrayAdapter adapp = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapp);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapp.remove(adapp.getItem(i));
            }
        });

        final String l1 = link1;
        final String l2 = link2;

        Button buton = (Button) findViewById(R.id.button);

        buton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                PApplet f = new PApplet();

                for (int i = 0; i < produsee.length; i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (produsee[i].equals(list.get(j))) {
                            deDus[tot++] = produseCoord[i];
                        }
                    }
                }

                final Intent inte = new Intent(ProductsList.this, MarcActivity.class);
                Bundle b = new Bundle();
                b.putStringArray("coord", deDus);
                b.putInt("n", tot);
                b.putString("nume", name);
                b.putString("link1",l1);
                b.putString("link2",l2);
                inte.putExtras(b);
                startActivity(inte);
            }
        });



    }
}
