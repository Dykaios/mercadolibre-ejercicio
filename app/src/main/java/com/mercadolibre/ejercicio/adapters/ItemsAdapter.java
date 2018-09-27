package com.mercadolibre.ejercicio.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.data.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by César Pardo on 20/09/2018.
 */
public class ItemsAdapter
    extends RecyclerView.Adapter<ItemsAdapter.ItemHolder> {

  private List<Item> items;
  private OnItemClickListener listener;

  public void setOnClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public void setItems(List<Item> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ItemsAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
    return new ItemHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ItemsAdapter.ItemHolder holder, int position) {
    // Set the values to the clientModel view holder
    Item item = items.get(position);
    Picasso
        .get()
        .load(item.getThumbnail())
        //I don't have resources for this
        //.placeholder(R.drawable.placeholder) //Display a place holder if the image it´s not loaded
        //.error(R.drawable.error) //Display a error icon if something went wrong.
        .resize(92, 92)
        .centerCrop()
        .into(holder.itemThumbnail);
    holder.itemDescription.setText(item.getTitle());
  }

  @Override
  public int getItemCount() {
    if (items == null) {
      items = new ArrayList<>();
    }
    return items.size();
  }

  class ItemHolder
      extends RecyclerView.ViewHolder {

    ImageView itemThumbnail;
    TextView itemDescription;

    ItemHolder(View itemView) {
      super(itemView);
      itemThumbnail = itemView.findViewById(R.id.item_thumbnail);
      itemDescription = itemView.findViewById(R.id.item_description);

      //Notified that the item its clicked.
      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(view, items.get(getAdapterPosition()));
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View view, Item item);
  }
}
