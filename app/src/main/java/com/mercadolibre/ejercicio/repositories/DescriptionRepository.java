package com.mercadolibre.ejercicio.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mercadolibre.ejercicio.data.models.Description;
import com.mercadolibre.ejercicio.webservices.Endpoints;
import com.mercadolibre.ejercicio.webservices.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class DescriptionRepository {
  private static final String TAG = "DescriptionRepository::";
  private Endpoints endpoints;

  public DescriptionRepository() {
    endpoints = Service.getEndpoints();
  }

  public void searchItemDescription(String itemId, MutableLiveData<Description> result) {
    Call<Description> call = endpoints.getItemDescription(itemId);
    call.enqueue(new Callback<Description>() {
      @Override
      public void onResponse(@NonNull Call<Description> call, @NonNull Response<Description> response) {
        result.postValue(response.body());
      }

      @Override
      public void onFailure(@NonNull Call<Description> call, @NonNull Throwable t) {
        Log.e(TAG, t.getMessage());
        result.postValue(null);
      }
    });
  }
}
