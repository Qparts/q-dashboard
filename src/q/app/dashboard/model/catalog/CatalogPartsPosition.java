package q.app.dashboard.model.catalog;

public class CatalogPartsPosition {

    private String number;
    private int[] coordinates;

    public int[] getCoordinates() {
        return coordinates;
    }

    public String getCoordinatesString(){
        try {
            return coordinates[0] + "," + coordinates[1] + "," + (coordinates[0] + coordinates[2]) + "," + (coordinates[1] + coordinates[3]);
        }catch (Exception ex){
            return "";
        }
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
