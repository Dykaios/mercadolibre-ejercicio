package com.mercadolibre.ejercicio.data.models;

import java.util.List;

/**
 * Created by César Pardo on 18/09/2018.
 */
public class Search {
  private List<Item> results;

  public List<Item> getResults() {
    return results;
  }

  public void setResults(List<Item> results) {
    this.results = results;
  }
}