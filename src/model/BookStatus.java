package model;

public enum BookStatus {

    ARCHIVED ("Списана"),

    ISSUED ("Выдана"),

    LOST ("Утерян"),

    AVAILABLE ("В наличии");


private String descript;
    BookStatus(String s) {
        this.descript = descript;

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
