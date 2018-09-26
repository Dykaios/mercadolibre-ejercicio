package com.mercadolibre.ejercicio.data.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.mercadolibre.ejercicio.data.models.Item;
import com.mercadolibre.ejercicio.webservices.Endpoints;
import com.mercadolibre.ejercicio.webservices.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class ItemRepository {
  private Endpoints endpoints;

  public ItemRepository() {
    endpoints = Service.getEndpoints();
  }

  public void searchItem(String itemId, MutableLiveData<Item> result) {
    Call<Item> call = endpoints.getItem(itemId);
    call.enqueue(new Callback<Item>() {
      @Override
      public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
        result.postValue(response.body());
      }

      @Override
      public void onFailure(@NonNull Call<Item> call, @NonNull Throwable t) {

      }
    });
  }
}
