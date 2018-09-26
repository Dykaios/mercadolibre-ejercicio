package com.mercadolibre.ejercicio.data.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mercadolibre.ejercicio.data.models.Item;
import com.mercadolibre.ejercicio.data.repositories.ItemRepository;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class ItemViewModel extends ViewModel {
  private ItemRepository itemRepository;
  private MutableLiveData<Item> item;

  public ItemViewModel() {
    itemRepository = new ItemRepository();
  }

  public void init(String itemId) {
    if (item != null) {
      return;
    }
    item = new MutableLiveData<>();
    itemRepository.searchItem(itemId, item);
  }

  public LiveData<Item> getItem() {
    return item;
  }
}
