package com.parse.parsestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;


public class ShippingView extends AppCompatActivity {

    EditText name, email, address, cityState, postalCode;
    ImageView poweredImage;
    Button checkoutImage;


    private Item product = MainActivity.selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_view);

        TextView priceLabel = (TextView) findViewById(R.id.priceLabel);
        ParseImageView productImage = (ParseImageView) findViewById(R.id.productImage);
        TextView productName = (TextView) findViewById(R.id.productName);
        TextView size = (TextView) findViewById(R.id.size);

        name = (EditText) findViewById(R.id.personName);
        email = (EditText) findViewById(R.id.personEmail);
        address = (EditText) findViewById(R.id.personAddress);
        cityState = (EditText) findViewById(R.id.cityState);
        postalCode = (EditText) findViewById(R.id.zipcode);

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

        poweredImage = (ImageView) findViewById(R.id.footer);
        poweredImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poweredImage.setColorFilter(R.color.primary, PorterDuff.Mode.MULTIPLY);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.parse.com"));
                startActivity(browserIntent);
            }
        });

        checkoutImage = (Button) findViewById(R.id.orderButton);
        checkoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() > 0 &&
                        email.getText().toString().length() > 0 &&
                        address.getText().toString().length() > 0 &&
                        cityState.getText().toString().length()  > 0 &&
                        postalCode.getText().toString().length() >  0){

                    Intent intent = new Intent(ShippingView.this,CheckoutView.class);
                    intent.putExtra("ShippingInfo", new ShippingInfo(name.getText().toString(),
                            email.getText().toString(), address.getText().toString(),
                            cityState.getText().toString(),postalCode.getText().toString()));
                    startActivity(intent);
                }
                else{
                    new AlertDialog.Builder(getApplication())
                            .setTitle("Missing Info")
                            .setMessage("Please fill out all the fields.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });
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
