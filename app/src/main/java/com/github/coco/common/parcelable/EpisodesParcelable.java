package com.github.coco.common.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created on 2022/1/8.
 *
 * @author wy
 */
@Data
public class EpisodesParcelable implements Parcelable {
    private String url;
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.url = source.readString();
        this.name = source.readString();
    }

    public EpisodesParcelable() {
    }

    protected EpisodesParcelable(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<EpisodesParcelable> CREATOR = new Parcelable.Creator<EpisodesParcelable>() {
        @Override
        public EpisodesParcelable createFromParcel(Parcel source) {
            return new EpisodesParcelable(source);
        }

        @Override
        public EpisodesParcelable[] newArray(int size) {
            return new EpisodesParcelable[size];
        }
    };
}
