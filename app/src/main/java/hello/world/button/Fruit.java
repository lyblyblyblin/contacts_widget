package hello.world.button;

public class Fruit {

    private String name;
    private int imageId;
    private boolean inDelete;

    public Fruit(String name, int imageId,boolean inDelete) {
        this.name = name;
        this.imageId = imageId;
        this.inDelete=inDelete;
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
