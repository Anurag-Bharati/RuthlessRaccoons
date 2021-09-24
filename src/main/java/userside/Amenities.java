package main.java.userside;

@SuppressWarnings("All")
public class Amenities {
    boolean WIFI=false, AC=false, ATTACHED=false, FAN=false, SINGLE=false, DOUBLE=false, TRIPLE=false;
    Amenities(boolean WIFI, boolean AC, boolean FAN, boolean ATTACHED, boolean SINGLE, boolean DOUBLE, boolean TRIPLE){
        this.WIFI = WIFI;
        this.AC = AC;
        this.ATTACHED = ATTACHED;
        this.FAN = FAN;
        this.SINGLE = SINGLE;
        this.DOUBLE = DOUBLE;
        this.TRIPLE = TRIPLE;
    }
    Amenities(){

    }

    public boolean hasWIFI() {
        return WIFI;
    }

    public void setWIFI(boolean WIFI) {
        this.WIFI = WIFI;
    }

    public boolean hasAC() {
        return AC;
    }

    public void setAC(boolean AC) {
        this.AC = AC;
    }

    public boolean isATTACHED() {
        return ATTACHED;
    }

    public void setATTACHED(boolean ATTACHED) {
        this.ATTACHED = ATTACHED;
    }

    public boolean hasFAN() {
        return FAN;
    }

    public void setFAN(boolean FAN) {
        this.FAN = FAN;
    }

    public boolean isSINGLE() {
        return SINGLE;
    }

    public void setSINGLE(boolean SINGLE) {
        this.SINGLE = SINGLE;
    }

    public boolean isDOUBLE() {
        return DOUBLE;
    }

    public void setDOUBLE(boolean DOUBLE) {
        this.DOUBLE = DOUBLE;
    }

    public boolean isTRIPLE() {
        return TRIPLE;
    }

    public void setTRIPLE(boolean TRIPLE) {
        this.TRIPLE = TRIPLE;
    }
}
