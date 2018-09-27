package com.mercadolibre.ejercicio.data.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mercadolibre.ejercicio.data.models.Search;
import com.mercadolibre.ejercicio.repositories.SearchRepository;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class SearchViewModel extends ViewModel {
  private SearchRepository searchRepository;
  private MutableLiveData<Search> search;

  public SearchViewModel() {
    searchRepository = new SearchRepository();
  }

  public void init() {
    if (search != null) {
      return;
    }
    search = new MutableLiveData<>();
  }

  public void search(String query) {
    searchRepository.doSearch(query, search);
  }

  public LiveData<Search> getSearch() {
    return search;
  }
}
