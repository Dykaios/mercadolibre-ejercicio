package com.mercadolibre.ejercicio.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import static com.mercadolibre.ejercicio.Constants.ITEMS_LIST;
import static com.mercadolibre.ejercicio.Constants.POSITION;
import static com.mercadolibre.ejercicio.Constants.RESULT_VS;
import static com.mercadolibre.ejercicio.Constants.SEARCHING_VS;
import static com.mercadolibre.ejercicio.Constants.SEARCH_TEXT;
import static com.mercadolibre.ejercicio.Constants.SEARCH_VS;
import static com.mercadolibre.ejercicio.Constants.WAITING_RESPONSE;

/**
 * Created by César Pardo on 20/09/2018.
 */
public class SearchFragment
    extends Fragment
    implements Callback<Search>, ItemsAdapter.OnItemClickListener {

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
  private boolean waitingResponse;

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
    adapter.setOnClickListener(this);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      glm = new GridLayoutManager(getContext(), 3);
    } else {
      glm = new GridLayoutManager(getContext(), 2);
    }

    resultRV.setLayoutManager(glm);
    resultRV.setAdapter(adapter);

    if (savedInstanceState != null && savedInstanceState.getBoolean(WAITING_RESPONSE)) {
      callSearch(savedInstanceState.getString(SEARCH_TEXT, ""));
    }
  }

  private void callSearch(String query) {
    waitingResponse = true;
    Endpoints endpoints = Service.getEndpoints();
    // Hardcoded Site_Id, only search on Mercado libre uruguay
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
    waitingResponse = false;
  }

  @Override
  public void onFailure(@NonNull Call<Search> call, @NonNull Throwable t) {
    Log.e("onFailure::", t.getMessage());
    waitingResponse = false;
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putParcelableArrayList(ITEMS_LIST, (ArrayList<Item>) adapter.getItems());
    outState.putInt(POSITION, glm.findFirstVisibleItemPosition());
    outState.putInt(SEARCH_VS, searchVS.getDisplayedChild());
    outState.putInt(SEARCHING_VS, searchingVS.getDisplayedChild());
    outState.putInt(RESULT_VS, resultVS.getDisplayedChild());
    outState.putString(SEARCH_TEXT, searchET.getText().toString());
    outState.putBoolean(WAITING_RESPONSE, waitingResponse);

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      adapter.setItems(savedInstanceState.<Item>getParcelableArrayList(ITEMS_LIST));
      glm.scrollToPosition(savedInstanceState.getInt(POSITION, 0));
      searchVS.setDisplayedChild(savedInstanceState.getInt(SEARCH_VS, 0));
      searchingVS.setDisplayedChild(savedInstanceState.getInt(SEARCHING_VS, 0));
      resultVS.setDisplayedChild(savedInstanceState.getInt(RESULT_VS, 0));
      searchET.setText(savedInstanceState.getString(SEARCH_TEXT, ""));
      waitingResponse = savedInstanceState.getBoolean(WAITING_RESPONSE);
    }
  }

  @Override
  public void onItemClick(View view, Item item) {
    FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, DetailFragment.newInstance(item.getId()));
    transaction.addToBackStack(null);
    transaction.commit();
  }
}
