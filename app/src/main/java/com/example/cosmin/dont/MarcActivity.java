package com.example.cosmin.dont;


import android.os.Bundle;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.support.v7.app.AppCompatActivity;


import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;

public class MarcActivity extends AppCompatActivity {
    private PApplet sketch;
    public static String[] seed;
    public static String[] elemente;
    public static String[] coord;
    static int lCoord;
    static String nume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String link1 = getIntent().getExtras().getString("link1");
        String link2 = getIntent().getExtras().getString("link2");
        coord = getIntent().getExtras().getStringArray("coord");
        lCoord = getIntent().getExtras().getInt("n");
        nume = getIntent().getExtras().getString("nume");


        sketch = new PApplet();
        elemente = sketch.loadStrings(link2);
        seed = sketch.loadStrings(link1);
        sketch = new skBun();

        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

//    static void gata() {
//        Intent intent = new Intent(MarcActivity.this, Harta.class);
//        startActivity(intent);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sketch != null) {
            sketch.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (sketch != null) {
            sketch.onBackPressed();
        }
    }
}