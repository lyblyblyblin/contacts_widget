package hello.world.button.Data;

import java.io.File;

public class Fruit {

    private String name;
    private int imageId;
    private File imageFile;
    private boolean inDelete;
    private File dir;

    public Fruit(String name, int imageId,boolean inDelete,File dir) {
        this.name = name;
        this.dir=dir;
        this.imageId = imageId;
        this.inDelete=inDelete;
    }
    public Fruit(String name, File imageFile,boolean inDelete,File dir) {
        this.name = name;
        this.dir=dir;
        this.imageFile = imageFile;
        this.inDelete=inDelete;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public File getImageFile() {
        return imageFile;
    }

    public boolean getinDelete() {
        return inDelete;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setInDelete(boolean inDelete) {
        this.inDelete = inDelete;
    }


}
