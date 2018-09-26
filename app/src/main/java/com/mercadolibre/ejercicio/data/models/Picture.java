package com.mercadolibre.ejercicio.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by César Pardo on 19/09/2018.
 */
public class Picture
    implements Parcelable {
  private String id;
  private String url;
  private String secure_url;
  private String size;
  private String max_size;
  private String quality;

  private Picture(Parcel in) {
    id = in.readString();
    url = in.readString();
    secure_url = in.readString();
    size = in.readString();
    max_size = in.readString();
    quality = in.readString();
  }

  public static final Creator<Picture> CREATOR = new Creator<Picture>() {
    @Override
    public Picture createFromParcel(Parcel in) {
      return new Picture(in);
    }

    @Override
    public Picture[] newArray(int size) {
      return new Picture[size];
    }
  };

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSecure_url() {
    return secure_url;
  }

  public void setSecure_url(String secure_url) {
    this.secure_url = secure_url;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getMax_size() {
    return max_size;
  }

  public void setMax_size(String max_size) {
    this.max_size = max_size;
  }

  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(id);
    parcel.writeString(url);
    parcel.writeString(secure_url);
    parcel.writeString(size);
    parcel.writeString(max_size);
    parcel.writeString(quality);
  }
}
