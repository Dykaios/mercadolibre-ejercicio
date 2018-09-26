package com.mercadolibre.ejercicio.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ViewSwitcher;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.adapters.ItemsAdapter;
import com.mercadolibre.ejercicio.data.models.Item;
import com.mercadolibre.ejercicio.data.view_models.SearchViewModel;

import java.util.List;
import java.util.Objects;

import static com.mercadolibre.ejercicio.Constants.POSITION;
import static com.mercadolibre.ejercicio.Constants.RESULT_VS;
import static com.mercadolibre.ejercicio.Constants.SEARCHING_VS;
import static com.mercadolibre.ejercicio.Constants.SEARCH_TEXT;
import static com.mercadolibre.ejercicio.Constants.SEARCH_VS;

/**
 * Created by César Pardo on 20/09/2018.
 */
public class SearchFragment
    extends Fragment
    implements ItemsAdapter.OnItemClickListener {

  private static final int SEARCHING = 0;
  private static final int RESULT = 1;
  private static final int NO_SEARCH = 0;
  private static final int PROGRESS = 1;
  private static final int ITEM_LIST = 0;
  private static final int EMPTY_LIST = 1;

  private TextInputEditText searchET;
  private ViewSwitcher searchVS;
  private ViewSwitcher searchingVS;
  private ViewSwitcher resultVS;

  private ItemsAdapter adapter;
  private GridLayoutManager glm;
  private int scrollPosition = 0;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_search, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    searchET = view.findViewById(R.id.search_text_input_edit_layout);
    searchVS = view.findViewById(R.id.search_view_switcher);
    searchingVS = view.findViewById(R.id.searching_view_switcher);
    resultVS = view.findViewById(R.id.result_view_switcher);

    SearchViewModel searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

    searchET.setOnEditorActionListener((textView, i, keyEvent) -> {
      if (EditorInfo.IME_ACTION_SEARCH == i) {
        searchVS.setDisplayedChild(SEARCHING);
        searchingVS.setDisplayedChild(PROGRESS);
        searchViewModel.search(searchET.getText().toString());
      }
      return false;
    });

    RecyclerView resultRV = view.findViewById(R.id.result_recycler);

    adapter = new ItemsAdapter();
    adapter.setOnClickListener(this);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      glm = new GridLayoutManager(getContext(), 3);
    } else {
      glm = new GridLayoutManager(getContext(), 2);
    }

    resultRV.setLayoutManager(glm);
    resultRV.setAdapter(adapter);

    searchViewModel.init();
    searchViewModel.getSearch().observe(Objects.requireNonNull(this), search -> {
      // update UI
      glm.scrollToPosition(scrollPosition);
      searchVS.setDisplayedChild(RESULT);
      searchingVS.setDisplayedChild(NO_SEARCH);
      List<Item> items = Objects.requireNonNull(search).getResults();
      if (null != items && items.size() > 0) {
        resultVS.setDisplayedChild(ITEM_LIST);
        adapter.setItems(items);
      } else {
        resultVS.setDisplayedChild(EMPTY_LIST);
      }
    });
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putInt(POSITION, glm.findFirstVisibleItemPosition());
    outState.putInt(SEARCH_VS, searchVS.getDisplayedChild());
    outState.putInt(SEARCHING_VS, searchingVS.getDisplayedChild());
    outState.putInt(RESULT_VS, resultVS.getDisplayedChild());
    outState.putString(SEARCH_TEXT, searchET.getText().toString());

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      scrollPosition = savedInstanceState.getInt(POSITION, 0);
      searchVS.setDisplayedChild(savedInstanceState.getInt(SEARCH_VS, 0));
      searchingVS.setDisplayedChild(savedInstanceState.getInt(SEARCHING_VS, 0));
      resultVS.setDisplayedChild(savedInstanceState.getInt(RESULT_VS, 0));
      searchET.setText(savedInstanceState.getString(SEARCH_TEXT, ""));
    }
  }

  @Override
  public void onItemClick(View view, Item item) {
    FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, DetailFragment.newInstance(item.getId()));
    transaction.addToBackStack(SearchFragment.class.getName());
    transaction.commit();
  }
}
