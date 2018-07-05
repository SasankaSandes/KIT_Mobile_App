package lk.sasadev.kitapp;


import com.google.android.gms.maps.model.LatLng;

public class Garage {
    String address;
    String name;
    String location;
    public Garage(){

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        float lat = Float.valueOf(location.substring(0,8));
        float lng = Float.valueOf(location.substring(9,18));
        LatLng latLng = new LatLng(lat,lng);
        return latLng;
    }
    public void setLocation(String location){
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

