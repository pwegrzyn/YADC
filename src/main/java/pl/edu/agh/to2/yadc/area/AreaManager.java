package pl.edu.agh.to2.yadc.area;


public class AreaManager {

    private Area currentArea;

    public void setCurrentArea(Area area) {
        this.currentArea = area;
    }

    public Area getCurrentArea() {
        return this.currentArea;
    }
}