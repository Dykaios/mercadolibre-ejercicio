package com.mercadolibre.ejercicio.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.adapters.PicturesAdapter;
import com.mercadolibre.ejercicio.data.models.Picture;
import com.mercadolibre.ejercicio.data.view_models.DescriptionViewModel;
import com.mercadolibre.ejercicio.data.view_models.ItemViewModel;

import java.util.List;
import java.util.Objects;

import static com.mercadolibre.ejercicio.Constants.PICTURE_POSITION;

/**
 * Created by César Pardo on 23/09/2018.
 */
public class DetailFragment extends Fragment {
  private static final String ITEM_ID = "ITEM_ID";

  private ViewSwitcher loadingViewSwitcher;
  private TextView picturesCount;
  private TextView title;
  private ViewPager picturesVP;

  private String itemId;
  private PicturesAdapter adapter;
  private boolean itemSearchEnded = false;
  private boolean descriptionSearchEnded = false;
  private int picturePosition = 0;

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

    loadingViewSwitcher = view.findViewById(R.id.details_view_switcher);
    picturesVP = view.findViewById(R.id.pictures_view_pager);
    picturesCount = view.findViewById(R.id.pictures_count);
    title = view.findViewById(R.id.item_title);
    TextView descriptionText = view.findViewById(R.id.item_description);

    adapter = new PicturesAdapter(getContext());

    picturesVP.setAdapter(adapter);

    //Use a LiveData to persist data against rotation screen.
    ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
    itemViewModel.init(itemId);
    //Get abstract about how the data its obtained.
    itemViewModel.getItem().observe(this, item -> {
      //Update UI
      itemSearchEnded = true;
      if (item == null) {
        Snackbar.make(view, R.string.error_getting_item, Snackbar.LENGTH_LONG).show();
        Objects.requireNonNull(getActivity()).onBackPressed();
      } else {
        List<Picture> pictures = Objects.requireNonNull(item).getPictures();
        if (pictures.size() > 0) {
          adapter.setItems(pictures);
        }
        picturesCount.setText(getString(R.string.picture_size, pictures.size()));
        title.setText(item.getTitle());

        if (descriptionSearchEnded && loadingViewSwitcher.getDisplayedChild() == 0) {
          loadingViewSwitcher.setDisplayedChild(1);
        }

        picturesVP.setCurrentItem(picturePosition);
      }
    });

    DescriptionViewModel descriptionViewModel = ViewModelProviders.of(this).get(DescriptionViewModel.class);
    descriptionViewModel.init(itemId);
    descriptionViewModel.getDescription().observe(this, description -> {
      //Update UI
      if (description == null) {
        Snackbar.make(view, R.string.error_getting_description, Snackbar.LENGTH_LONG).show();
        Objects.requireNonNull(getActivity()).onBackPressed();
      } else {
        descriptionSearchEnded = true;
        loadingViewSwitcher.setDisplayedChild(1);
        descriptionText.setText(Objects.requireNonNull(description).getPlain_text());
        if (itemSearchEnded && loadingViewSwitcher.getDisplayedChild() == 0) {
          loadingViewSwitcher.setDisplayedChild(1);
        }
      }
    });
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putInt(PICTURE_POSITION, picturesVP.getCurrentItem());
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      picturePosition = savedInstanceState.getInt(PICTURE_POSITION, 0);
    }
  }


}
