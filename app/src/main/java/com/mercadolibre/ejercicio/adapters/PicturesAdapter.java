package com.mercadolibre.ejercicio.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.data.models.Picture;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by César Pardo on 23/09/2018.
 */
public class PicturesAdapter
    extends PagerAdapter {

  private List<Picture> items;
  private Context context;

  public PicturesAdapter(Context context) {
    this.context = context;
    items = new ArrayList<>();
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  public void setItems(List<Picture> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Picture item = items.get(position);
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.picture_line, container, false);

    ImageView itemPicture = layout.findViewById(R.id.item_picture);
    final ViewSwitcher viewSwitcher = layout.findViewById(R.id.picture_view_switcher);
    final String[] size = item.getSize().split("x");
    Picasso
        .get()
        .load(item.getUrl())
        //.placeholder(R.drawable.thumnail)
        //.error(R.drawable.thumnail)
        .resize(Integer.parseInt(size[0]), Integer.parseInt(size[1]))
        .centerCrop()
        .into(itemPicture, new Callback() {
          @Override
          public void onSuccess() {
            viewSwitcher.setDisplayedChild(1);
          }

          @Override
          public void onError(Exception e) {

          }
        });
    container.addView(layout);
    return layout;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }
}
