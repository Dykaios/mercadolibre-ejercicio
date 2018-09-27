package com.mercadolibre.ejercicio.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.mercadolibre.ejercicio.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.mercadolibre.ejercicio.Constants.SEARCH_TEXT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by César Pardo on 27/09/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class SearchFragmentTest {
  private SearchFragment subject;

  @Before
  public void setup() {
    subject = new SearchFragment();
    startFragment(subject);
  }

  @Test
  public void testFragment() {
    assertNotNull(subject);
  }

  @Test
  public void testView() {
    View view = subject.getView();
    assertNotNull(view);
  }

  @Test
  public void onSavedInstanceState_outState_fieldsSavedIntoOutState() {
    View view = subject.getView();
    Bundle outState = new Bundle();
    TextInputEditText searchET = view.findViewById(R.id.search_text_input_edit_layout);
    searchET.setText("some-text");
    subject.onSaveInstanceState(outState);
    assertEquals("some-text", outState.getString(SEARCH_TEXT, ""));
  }

  @Test
  public void onViewStateRestored_savedInstanceState_fieldRestoredFromSavedInstanceState() {
    View view = subject.getView();
    Bundle savedInstanceState = new Bundle();
    savedInstanceState.putString(SEARCH_TEXT, "another-text");
    subject.onViewStateRestored(savedInstanceState);
    TextInputEditText searchET = view.findViewById(R.id.search_text_input_edit_layout);
    assertEquals("another-text", searchET.getText().toString());
  }
}
