package com.parse.parsestore;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static Item selectedItem;

    final String LOGTAG = "MainActivity";
    final ArrayList<Item> items = new ArrayList<>();

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ImageView poweredImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataLocally();

    }

    private void loadDataLocally() {
        Log.i(LOGTAG, "Loading data locally!");
        ParseQuery<Item> localQuery = ParseQuery.getQuery(Item.class);
        localQuery.fromLocalDatastore();
        localQuery.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (Item item : objects) {
                        items.add(item);
                    }
                } else {
                    Log.d(LOGTAG, "Exception, while querying the Items List from Local database." + e.getMessage());
                }
                if (items == null || items.size() == 0)
                    loadFromNetwork();
                else {
                    Log.i(LOGTAG, "Finished loading local data.");
                    display();
                }
            }
        });
    }

    private void loadFromNetwork() {
        Log.i(LOGTAG, "Loading data from the Network!");
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.orderByDescending("description");
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    for (Item item : objects) {
                        items.add(item);
                        item.pinInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i(LOGTAG, "Saving data Locally!");
                            }
                        });
                    }
                    display();
                } else {
                    Log.d("LOGTAG", "Exception, while querying the Items List from Parse database." + e.getMessage());
                }
            }
        });
    }

    private void display() {
        setContentView(R.layout.activity_main);
        poweredImage = (ImageView) findViewById(R.id.footer);
        poweredImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poweredImage.setColorFilter(R.color.primary_text, PorterDuff.Mode.MULTIPLY);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.parse.com"));
                startActivity(browserIntent);
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        ProductsView productsView = new ProductsView(items);
        productsView.setOrderButtonListener(new ProductsView.OrderButtonListener() {
            @Override
            public void orderButtonClicked(Item item) {
                selectedItem = item;
                Intent intent = new Intent(MainActivity.this,ShippingView.class);
                startActivity(intent);
            }
        });

        mAdapter = productsView;
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
