package hello.world.button.Data;

import android.graphics.Bitmap;

public class MyContacts {

    private String name;
    private String num;
    private int imageId;
    private Bitmap imageBitmap;

//    public contact(String name, String num, int imageId) {
//        this.num = num;
//        this.name = name;
//        this.imageId = imageId;
//    }
    public MyContacts(String name, String num, Bitmap imageBitmap) {
        this.num = num;
        this.name = name;
        this.imageBitmap = imageBitmap;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public int getImageId() {
        return imageId;
    }

    public String getNum() {
        return num;
    }
}
