package com.mercadolibre.ejercicio.webservices;

import com.mercadolibre.ejercicio.models.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by César Pardo on 18/09/2018.
 */
public interface Endpoints {
  @GET("/sites/{Site_id}/search")
  Call<Search> searchItem(@Path("Site_id") String siteId, @Query("q") String query);
}
