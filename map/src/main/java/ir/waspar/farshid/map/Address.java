package ir.waspar.farshid.map;

public class Address {
    private String state;
    private String county;
    private String city;
    private String zone;
    private String district;
    private String village;
    private String other;
    private String ways;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getWays() {
        return ways;
    }

    public void setWays(String ways) {
        this.ways = ways;
    }

    @Override
    public String toString() {
        return "Address{" +
                "state='" + state + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", zone='" + zone + '\'' +
                ", district='" + district + '\'' +
                ", village='" + village + '\'' +
                ", other='" + other + '\'' +
                ", ways='" + ways + '\'' +
                '}';
    }
}
