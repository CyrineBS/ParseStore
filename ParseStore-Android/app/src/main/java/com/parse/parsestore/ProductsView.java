package com.parse.parsestore;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madhav on 7/11/15.
 */
public class ProductsView extends RecyclerView.Adapter<ProductsView.ViewHolder>{

    List<Item> mProducts;

    public ProductsView(ArrayList<Item> items){
        mProducts = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductsView.ViewHolder viewHolder, int i) {
        Item products = mProducts.get(i);

        if(!products.hasSize())
            viewHolder.sizes.setVisibility(View.INVISIBLE);

        viewHolder.productName.setText(products.getDescription());
        viewHolder.priceLabel.setText("$" + products.getPrice().toString());
        ParseFile itemImage = products.getItemImage();
        if(itemImage!= null){
            viewHolder.productImage.setParseFile(itemImage);
            viewHolder.productImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                }
            });
        }




    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView priceLabel;
        public ParseImageView productImage;
        public TextView productName;
        public Spinner sizes;
        public Button orderButton;

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
