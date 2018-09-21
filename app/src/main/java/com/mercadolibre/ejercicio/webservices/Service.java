package com.mercadolibre.ejercicio.webservices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by César Pardo on 16/05/2018.
 * REST Client singleton
 */
public class Service {
  private static Retrofit retrofit;
  private static Service service;

  private Service() {
    retrofit = new Retrofit.Builder()
        .baseUrl("https://api.mercadolibre.com/sites/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  public static Endpoints getEndpoints() {
    if (service == null) {
      service = new Service();
    }
    return retrofit.create(Endpoints.class);
  }
}
