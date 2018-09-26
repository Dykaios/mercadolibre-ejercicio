package com.mercadolibre.ejercicio.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mercadolibre.ejercicio.R;
import com.mercadolibre.ejercicio.fragments.SearchFragment;

public class MainActivity
    extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState != null) {
      return;
    }
    
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.fragment_container, new SearchFragment())
        .commit();
  }
}
