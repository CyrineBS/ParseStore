package com.parse.parsestore.Activities;

/**
 * Created by Madhav Chhura on 7/11/15.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.parsestore.Item;
import com.parse.parsestore.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends RecyclerView.Adapter<ProductsActivity.ViewHolder>{

    List<Item> mProducts;
    Context mContext;

    private OrderButtonListener orderButtonListener;

    public ProductsActivity(ArrayList<Item> items){
        mProducts = items;
    }

    public void setOrderButtonListener(OrderButtonListener listener){
        this.orderButtonListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsActivity.ViewHolder viewHolder, int i) {
        final Item product = mProducts.get(i);

        if(!product.hasSize())
            viewHolder.sizes.setVisibility(View.INVISIBLE);

        viewHolder.productName.setText(product.getDescription());
        viewHolder.priceLabel.setText("$" + product.getPrice().toString());
        ParseFile itemImage = product.getItemImage();
        if(itemImage!= null){
            viewHolder.productImage.setParseFile(itemImage);
            viewHolder.productImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                }
            });
        }
        viewHolder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.hasSize() && viewHolder.sizes.getSelectedItemPosition() < 1) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Missing Size")
                            .setMessage("Please select a size.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                else {
                    product.setSize(viewHolder.sizes.getSelectedItem().toString());
                    orderButtonListener.orderButtonClicked(product);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public interface OrderButtonListener {
        void orderButtonClicked(Item item);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView priceLabel;
        private ParseImageView productImage;
        private TextView productName;
        private Spinner sizes;
        private Button orderButton;

        public ViewHolder(View itemView) {
            super(itemView);
            priceLabel = (TextView) itemView.findViewById(R.id.priceLabel);
            productImage = (ParseImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            sizes = (Spinner) itemView.findViewById(R.id.sizeList);
            orderButton = (Button) itemView.findViewById(R.id.orderButton);
        }
    }
}
