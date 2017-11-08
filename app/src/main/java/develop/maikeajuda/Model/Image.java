package develop.maikeajuda.Model;

import android.graphics.Bitmap;


public class Image {
    private String name;
    private String data;
    private Bitmap picture;

    public Image(String name, String data, Bitmap picture) {
        this.name = name;
        this.data = data;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
