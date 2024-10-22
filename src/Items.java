abstract class Items {
     String barcode;
     String title;
     String type; // "Book" or "Multimedia"

    public Items(String barcode, String title, String type) {
        this.barcode = barcode;
        this.title = title;
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }


}
