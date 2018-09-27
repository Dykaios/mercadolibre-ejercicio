package com.mercadolibre.ejercicio.data.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.mercadolibre.ejercicio.data.models.Description;
import com.mercadolibre.ejercicio.repositories.DescriptionRepository;

/**
 * Created by César Pardo on 25/09/2018.
 */
public class DescriptionViewModel extends ViewModel {
  private DescriptionRepository descriptionRepository;
  private MutableLiveData<Description> description;

  public DescriptionViewModel() {
    descriptionRepository = new DescriptionRepository();
  }

  public void init(String itemId) {
    if (description != null) {
      return;
    }
    description = new MutableLiveData<>();
    descriptionRepository.searchItemDescription(itemId, description);
  }

  public LiveData<Description> getDescription() {
    return description;
  }
}
