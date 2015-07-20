package com.parse.parsestore.Activities;

/**
 * Created by Madhav Chhura on 7/19/15.
 */

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.parsestore.Fragments.ErrorDialogFragment;
import com.parse.parsestore.Fragments.ProgressDialogFragment;
import com.parse.parsestore.Item;
import com.parse.parsestore.R;
import com.parse.parsestore.ShippingInfo;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {

    //Enter your Stripe test publishable key below.
    public static final String PUBLISHABLE_KEY = "";

    Item selectedItem = MainActivity.selectedItem;
    ShippingInfo shippingInfo;
    Card card;
    String cardType;
    LinearLayout myLayout;
    EditText cardNumberField, cvc, expDate, zipcode;
    private ProgressDialogFragment progressFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle data = getIntent().getExtras();
        shippingInfo = data.getParcelable("ShippingInfo");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Checkout");
        toolbar.inflateMenu(R.menu.menu_checkout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.buy) {
                    buy();
                }
                return true;
            }
        });

        myLayout = (LinearLayout) findViewById(R.id.creditCardInfoLayout);
        myLayout.setVisibility(View.INVISIBLE);

        cvc = (EditText) findViewById(R.id.cvc);
        expDate = (EditText) findViewById(R.id.expDate);
        cardNumberField = (EditText) findViewById(R.id.cardNumber);
        zipcode = (EditText) findViewById(R.id.zipcode);

        progressFragment = ProgressDialogFragment.newInstance(R.string.authorizing);

        final TextView last4 = (TextView) findViewById(R.id.last4);

        cardNumberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cardNumberField.setTextColor(getResources().getColor(R.color.secondary_text));
                String working = s.toString();
                if (working.length() >= 2) {
                    card = new Card(working, 0, 0, null);
                    cardType = card.getType();
                    switch (cardType) {

                        case "American Express":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.amex, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.amex, 0, 0, 0);
                            break;
                        case "Visa":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visa, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visa, 0, 0, 0);
                            break;
                        case "Discover":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.discover, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.discover, 0, 0, 0);
                            break;
                        case "JCB":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jcb, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jcb, 0, 0, 0);
                            break;
                        case "Diners Club":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diners, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.diners, 0, 0, 0);
                            break;
                        case "MasterCard":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mastercard, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mastercard, 0, 0, 0);
                            break;
                        case "Unknown":
                            cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                            last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                            break;

                    }
                    if (card.validateNumber()) {
                        cardNumberField.setVisibility(View.INVISIBLE);
                        myLayout.setVisibility(View.VISIBLE);
                        last4.setText(card.getLast4());
                    } else if (cardType.equals("American Express") && working.length() == 15) {
                        cardNumberField.setError("Invalid Card Number");
                        cardNumberField.setTextColor(getResources().getColor(R.color.red));
                    } else if (working.length() == 16) {
                        cardNumberField.setError("Invalid Card Number");
                        cardNumberField.setTextColor(getResources().getColor(R.color.red));
                    }
                } else if (working.length() == 0) {
                    cardNumberField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                    last4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.creditcard_placeholder, 0, 0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        last4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLayout.setVisibility(View.INVISIBLE);
                cardNumberField.setVisibility(View.VISIBLE);
            }
        });
        
        expDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();

                if (working.length() == 2 && before == 0) {
                    card.setExpMonth(Integer.parseInt(working));
                    working += "/";
                    expDate.setText(working);
                    expDate.setSelection(working.length());
                } else if (working.length() == 5 && before == 0) {
                    card.setExpYear(Integer.parseInt(working.substring(3)));
                    cvc.requestFocus();
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

                if (card.getType().equals("American Express") && working.length() == 4) {
                    card.setCVC(working);
                    zipcode.requestFocus();
                } else if (working.length() == 3) {
                    card.setCVC(working);
                    zipcode.requestFocus();
                }
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final ImageView poweredImage = (ImageView) findViewById(R.id.footer);
        poweredImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poweredImage.setColorFilter(R.color.primary, PorterDuff.Mode.MULTIPLY);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.parse.com"));
                startActivity(browserIntent);
            }
        });
    }
    private void buy() {
        boolean validation = card.validateCard();
        if(validation){
            startProgress();
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        @Override
                        public void onSuccess(Token token) {
                            finishProgress();
                            charge(token);
                        }
                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            //finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    private void charge(Token cardToken){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("itemName", selectedItem.getName());
        params.put("size", (selectedItem.hasSize() ? selectedItem.getSize() : "N/A"));
        params.put("cardToken", cardToken.getId());
        params.put("name",shippingInfo.getName());
        params.put("email",shippingInfo.getEmail());
        params.put("address",shippingInfo.getAddress());
        params.put("zip",shippingInfo.getPostalCode());
        params.put("city_state",shippingInfo.getCityState());

        progressFragment = ProgressDialogFragment.newInstance(R.string.charging);
        startProgress();
        ParseCloud.callFunctionInBackground("purchaseItem", params, new FunctionCallback<Object>() {
            public void done(Object response, ParseException e) {
                finishProgress();
                if (e == null) {
                    Log.d("Cloud Response", "There were no exceptions! " + response.toString());
                    Toast.makeText(getApplicationContext(),
                            "Item Purchased Successfully ",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Cloud Response", "Exception: " + e);
                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startProgress() {
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    private void finishProgress() {
        progressFragment.dismiss();
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }
}

