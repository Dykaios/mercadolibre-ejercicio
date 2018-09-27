package com.mercadolibre.ejercicio.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mercadolibre.ejercicio.data.models.Search;
import com.mercadolibre.ejercicio.webservices.Endpoints;
import com.mercadolibre.ejercicio.webservices.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class SearchRepository {
  private static final String TAG = "SearchRepository::";
  private Endpoints endpoints;

  public SearchRepository() {
    endpoints = Service.getEndpoints();
  }

  public void doSearch(String query, MutableLiveData<Search> result) {
    // Hardcoded Site_Id, only search on Mercado libre uruguay
    Call<Search> call = endpoints.searchItem("MLU", query);
    call.enqueue(new Callback<Search>() {
      @Override
      public void onResponse(@NonNull Call<Search> call, @NonNull Response<Search> response) {
        result.postValue(response.body());
      }

      @Override
      public void onFailure(@NonNull Call<Search> call, @NonNull Throwable t) {
        Log.e(TAG, t.getMessage());
        result.postValue(null);
      }
    });
  }
}
