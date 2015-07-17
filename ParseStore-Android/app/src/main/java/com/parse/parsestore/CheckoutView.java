package com.parse.parsestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.stripe.android.model.Card;


public class CheckoutView extends AppCompatActivity {

    ShippingInfo shippingInfo;
    Card card;
    String cardType;

    EditText creditCardNumber, cvc, expDate, zipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle data = getIntent().getExtras();
        shippingInfo = data.getParcelable("shippingInfo");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Checkout");

        final LinearLayout myLayout = (LinearLayout) findViewById(R.id.creditCardInfoLayout);
        myLayout.setVisibility(View.INVISIBLE);

        cvc = (EditText) findViewById(R.id.cvc);
        expDate = (EditText) findViewById(R.id.expDate);
        creditCardNumber = (EditText) findViewById(R.id.cardNumber);
        zipcode = (EditText) findViewById(R.id.zipcode);

        creditCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                creditCardNumber.setTextColor(getResources().getColor(R.color.secondary_text));
                String working = s.toString();
                if (working.length() >= 2) {
                    card = new Card(working, 0, 0, null);
                    cardType = card.getType();
                    switch (cardType) {

                        case "American Express":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.amex, 0, 0, 0);
                            break;
                        case "Visa":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visa, 0, 0, 0);
                            break;
                        case "Discover":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.discover, 0, 0, 0);
                            break;
                        case "JCB":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jcb, 0, 0, 0);
                            break;
                        case "Diners Club":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diners, 0, 0, 0);
                            break;
                        case "MasterCard":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mastercard, 0, 0, 0);
                            break;
                        case "Unknown":
                            creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                            break;

                    }
                    if(card.validateNumber()){
                        creditCardNumber.setVisibility(View.INVISIBLE);
                        myLayout.setVisibility(View.VISIBLE);
                    }
                    else if(cardType.equals("American Express") && working.length() ==  15){
                        creditCardNumber.setError("Invalid Card Number");
                        creditCardNumber.setTextColor(getResources().getColor(R.color.red));
                    }
                    else if(working.length() == 16){
                        creditCardNumber.setError("Invalid Card Number");
                        creditCardNumber.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                else if(working.length() == 0) {
                    creditCardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        expDate.setMaxEms(4);
        expDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                boolean isValid = true;
                if (working.length() == 2 && before == 0) {
                    card.setExpMonth(Integer.parseInt(working));
                    if (!card.validateExpMonth()) {
                        isValid = false;
                    } else {
                        working += "/";
                        expDate.setText(working);
                        expDate.setSelection(working.length());
                    }
                } else if (working.length() == 5 && before == 0) {
                    card.setExpYear(Integer.parseInt(working.substring(3)));
                    if (!card.validateExpYear())
                        isValid = false;
                    else
                        cvc.requestFocus();

                } else if (working.length() != 5)
                    isValid = false;

                if (!isValid) {
                    expDate.setError("Enter a valid exp date: MM/YY");
                } else {
                    expDate.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                card.setCVC(working);

                if(card.getType().equals("American Express") && working.length() == 4){
                    if(!card.validateCVC())
                        cvc.setError("Invalid CVC");
                    else
                        zipcode.requestFocus();
                }
                if(card.validateCVC() && working.length() == 3)
                    zipcode.requestFocus();
                else
                    cvc.setError("Invalid CVC");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = zipcode.getText().toString();
                if(working.length() == 5)
                    card.setAddressZip(working);
                else
                    zipcode.setError("Invalid Zip");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
