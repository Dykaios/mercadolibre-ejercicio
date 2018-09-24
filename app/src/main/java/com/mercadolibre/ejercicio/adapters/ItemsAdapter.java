package com.mercadolibre.ejercicio.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.models.Item;
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

  public ItemsAdapter() {
    items = new ArrayList<>();
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
        //.placeholder(R.drawable.thumnail)
        //.error(R.drawable.thumnail)
        .resize(92, 92)
        .centerCrop()
        .into(holder.itemThumbnail);
    holder.itemDescription.setText(item.getTitle());
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public List<Item> getItems() {
    return items;
  }

  class ItemHolder
      extends RecyclerView.ViewHolder {

    ImageView itemThumbnail;
    TextView itemDescription;

    ItemHolder(View itemView) {
      super(itemView);
      itemThumbnail = itemView.findViewById(R.id.item_thumbnail);
      itemDescription = itemView.findViewById(R.id.item_description);

      //Se notifica al fragment que un elemento fue seleccionado.
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (listener != null) {
            listener.onItemClick(view, items.get(getAdapterPosition()));
          }
        }
      });
    }
  }

  public interface OnItemClickListener {
    void onItemClick(View view, Item item);
  }
}
