package com.parse.parsestore.Activities;

/**
 * Created by Madhav Chhura on 7/11/15.
 */

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.parsestore.Fragments.ErrorDialogFragment;
import com.parse.parsestore.Item;
import com.parse.parsestore.R;
import com.parse.parsestore.ShippingInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShippingActivity extends AppCompatActivity {

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
                if(isEmailValid(email.getText().toString())){

                    Intent intent = new Intent(ShippingActivity.this,CheckoutActivity.class);
                    intent.putExtra("ShippingInfo", new ShippingInfo(name.getText().toString(),
                            email.getText().toString(), address.getText().toString(),
                            cityState.getText().toString(),postalCode.getText().toString()));
                    startActivity(intent);
                }
                else {
                    DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.invalidEmail, "Please enter a valid email.");
                    fragment.show(getSupportFragmentManager(), "Invalid Email");
                }
            }
        });

    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
