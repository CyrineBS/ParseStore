package com.parse.parsestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;


public class ShippingView extends AppCompatActivity {

    private TextView priceLabel;
    private ParseImageView productImage;
    private TextView productName;
    private TextView size;

    private Item product = MainActivity.selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_view);

        priceLabel = (TextView) findViewById(R.id.priceLabel);
        productImage = (ParseImageView) findViewById(R.id.productImage);
        productName = (TextView) findViewById(R.id.productName);
        size = (TextView) findViewById(R.id.size);

        if(!product.hasSize())
            size.setVisibility(View.INVISIBLE);

        productName.setText(product.getDescription());
        priceLabel.setText("$" + product.getPrice().toString());
        ParseFile itemImage = product.getItemImage();
        if(itemImage!= null){
            productImage.setParseFile(itemImage);
            productImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shipping_view, menu);
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
