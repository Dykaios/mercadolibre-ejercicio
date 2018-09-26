package com.mercadolibre.ejercicio.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by César Pardo on 23/09/2018.
 */
public class Description
    implements Parcelable {
  private String text;
  private String plain_text;

  protected Description(Parcel in) {
    text = in.readString();
    plain_text = in.readString();
  }

  public static final Creator<Description> CREATOR = new Creator<Description>() {
    @Override
    public Description createFromParcel(Parcel in) {
      return new Description(in);
    }

    @Override
    public Description[] newArray(int size) {
      return new Description[size];
    }
  };

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getPlain_text() {
    return plain_text;
  }

  public void setPlain_text(String plain_text) {
    this.plain_text = plain_text;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(text);
    parcel.writeString(plain_text);
  }
}
