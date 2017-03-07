package com.example.yoyo.databaseapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoyo on 4/20/2016.
 */
public class Notes implements Parcelable
{
   private int _id;
   private String topic;
   private String description;
    //private byte[] image;
    private String picturepath;
    private String lat;
    private String lng;

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Notes(String lng, int _id, String topic, String description, String picturepath, String lat) {
        this.lng = lng;
        this._id = _id;
        this.topic = topic;
        this.description = description;
        this.picturepath = picturepath;
        this.lat = lat;
    }

    public Notes(Parcel source)
    {
        _id = source.readInt();
        topic = source.readString();
        description = source.readString();
        picturepath = source.readString();
        lat = source.readString();
        lng = source.readString();
    }

    public Notes()
    {

    }

    public Notes(int _id, String topic, String description)
    {
        this._id = _id;
        this.topic = topic;
        this.description = description;
    }

    public Notes(int _id, String topic, String description, String image) {
        this._id = _id;
        this.topic = topic;
        this.description = description;
        picturepath = image;
    }

    public String getImage() {
        return picturepath;
    }

    public void setImage(String path) {
        picturepath = path;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "_id=" + _id +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", picturepath='" + picturepath + '\'' +
                '}';
    }

    public Notes(String topic, String description)
    {
        this.topic = topic;
        this.description = description;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(_id);
        dest.writeString(topic);
        dest.writeString(description);
        dest.writeString(picturepath);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
