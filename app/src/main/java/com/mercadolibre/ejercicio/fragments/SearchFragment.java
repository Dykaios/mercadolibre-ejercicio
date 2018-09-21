package com.mercadolibre.ejercicio.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.adapters.ItemsAdapter;
import com.mercadolibre.ejercicio.models.Item;
import com.mercadolibre.ejercicio.models.Search;
import com.mercadolibre.ejercicio.webservices.Endpoints;
import com.mercadolibre.ejercicio.webservices.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Pardo on 20/09/2018.
 */
public class SearchFragment
    extends Fragment
    implements Callback<Search> {

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

    searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_SEARCH == i) {
          searchVS.setDisplayedChild(SEARCHING);
          searchingVS.setDisplayedChild(PROGRESS);
          callSearch(searchET.getText().toString());
        }
        return false;
      }
    });

    RecyclerView resultRV = view.findViewById(R.id.result_recycler);

    adapter = new ItemsAdapter();

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      glm = new GridLayoutManager(getContext(), 3);
    } else {
      glm = new GridLayoutManager(getContext(), 2);
    }
    resultRV.setLayoutManager(glm);
    resultRV.setAdapter(adapter);

    if (savedInstanceState != null) {
      callSearch(savedInstanceState.getString("SEARCH_TEXT", ""));
    }
  }

  private void callSearch(String query) {
    Endpoints endpoints = Service.getEndpoints();
    // Hardcoded Site_Id para solo buscar en Mercado libre uruguay
    Call<Search> call = endpoints.searchItem("MLU", query);
    call.enqueue(SearchFragment.this);
  }

  @Override
  public void onResponse(@NonNull Call<Search> call, @NonNull Response<Search> response) {
    searchVS.setDisplayedChild(RESULT);
    searchingVS.setDisplayedChild(NO_SEARCH);
    if (response.body() != null) {
      List<Item> items = Objects.requireNonNull(response.body()).getResults();
      if (null != items && items.size() > 0) {
        resultVS.setDisplayedChild(ITEM_LIST);
        adapter.setItems(items);
      } else {
        resultVS.setDisplayedChild(EMPTY_LIST);
      }
    }
  }

  @Override
  public void onFailure(@NonNull Call<Search> call, @NonNull Throwable t) {
    Log.e("onFailure::", t.getMessage());
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putParcelableArrayList("ITEM_LIST", (ArrayList<Item>) adapter.getItems());
    outState.putInt("POSITION", glm.findFirstVisibleItemPosition());
    outState.putInt("SEARCHVS", searchVS.getDisplayedChild());
    outState.putInt("SEARCHINGVS", searchingVS.getDisplayedChild());
    outState.putInt("RESULTVS", resultVS.getDisplayedChild());
    outState.putString("SEARCH_TEXT", searchET.getText().toString());

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      adapter.setItems(savedInstanceState.<Item>getParcelableArrayList("ITEM_LIST"));
      glm.scrollToPosition(savedInstanceState.getInt("POSITION", 0));
      searchVS.setDisplayedChild(savedInstanceState.getInt("SEARCHVS", 0));
      searchingVS.setDisplayedChild(savedInstanceState.getInt("SEARCHINGVS", 0));
      resultVS.setDisplayedChild(savedInstanceState.getInt("RESULTVS", 0));
      searchET.setText(savedInstanceState.getString("SEARCH_TEXT", ""));
    }
  }
}
