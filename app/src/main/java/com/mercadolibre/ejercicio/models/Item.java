package com.mercadolibre.ejercicio.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by César Pardo on 18/09/2018.
 */
public class Item
    implements Parcelable {
  private String id;
  private String title;
  private List<Description> descriptions;
  private String thumbnail;
  private List<Picture> pictures;


  protected Item(Parcel in) {
    id = in.readString();
    title = in.readString();
    descriptions = in.createTypedArrayList(Description.CREATOR);
    thumbnail = in.readString();
    pictures = in.createTypedArrayList(Picture.CREATOR);
  }

  public static final Creator<Item> CREATOR = new Creator<Item>() {
    @Override
    public Item createFromParcel(Parcel in) {
      return new Item(in);
    }

    @Override
    public Item[] newArray(int size) {
      return new Item[size];
    }
  };

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Description> getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(List<Description> descriptions) {
    this.descriptions = descriptions;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public void setPictures(List<Picture> pictures) {
    this.pictures = pictures;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(id);
    parcel.writeString(title);
    parcel.writeTypedList(descriptions);
    parcel.writeString(thumbnail);
    parcel.writeTypedList(pictures);
  }
}
