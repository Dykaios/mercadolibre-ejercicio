package com.mercadolibre.ejercicio.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.adapters.PicturesAdapter;
import com.mercadolibre.ejercicio.models.Description;
import com.mercadolibre.ejercicio.models.Item;
import com.mercadolibre.ejercicio.models.Picture;
import com.mercadolibre.ejercicio.webservices.Endpoints;
import com.mercadolibre.ejercicio.webservices.Service;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Pardo on 23/09/2018.
 */
public class DetailFragment extends Fragment {
  private static final String ITEM_ID = "ITEM_ID";

  private ViewSwitcher viewSwitcher;
  private TextView picturesCount;
  private TextView title;
  private TextView description;

  private String itemId;
  private PicturesAdapter adapter;

  public static DetailFragment newInstance(String itemId) {
    DetailFragment fragment = new DetailFragment();
    Bundle args = new Bundle();
    args.putString(ITEM_ID, itemId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      itemId = getArguments().getString(ITEM_ID);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_detail, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    viewSwitcher = view.findViewById(R.id.details_view_switcher);
    ViewPager picturesVP = view.findViewById(R.id.pictures_view_pager);
    picturesCount = view.findViewById(R.id.pictures_count);
    title = view.findViewById(R.id.item_title);
    description = view.findViewById(R.id.item_description);

    adapter = new PicturesAdapter(getContext());

    picturesVP.setAdapter(adapter);

    final Endpoints endpoints = Service.getEndpoints();
    Call<Item> call = endpoints.getItem(itemId);
    call.enqueue(new Callback<Item>() {
      @Override
      public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
        if (response.body() != null) {
          List<Picture> pictures = Objects.requireNonNull(response.body()).getPictures();
          if (pictures.size() > 0) {
            adapter.setItems(pictures);
          }
          picturesCount.setText(getString(R.string.picture_size, pictures.size()));
          title.setText(Objects.requireNonNull(response.body()).getTitle());

          Call<Description> callDescription = endpoints.getItemDescription(itemId);
          callDescription.enqueue(new Callback<Description>() {
            @Override
            public void onResponse(@NonNull Call<Description> call, @NonNull Response<Description> response) {
              viewSwitcher.setDisplayedChild(1);
              if (response.body() != null)
                description.setText(Objects.requireNonNull(response.body()).getPlain_text());
            }

            @Override
            public void onFailure(@NonNull Call<Description> call, @NonNull Throwable t) {
              viewSwitcher.setDisplayedChild(1);
            }
          });
        }
      }

      @Override
      public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {
        viewSwitcher.setDisplayedChild(1);
      }
    });
  }
}
