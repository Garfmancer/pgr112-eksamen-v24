package objects;


import java.sql.Date;

public class ItemBase {
    private int id;
    private String discoveredLocation;
    private int discoveredByPersonId;


    private Date discoveredDate;
    private int estimatedAge;
    private Integer museumId;
    private String classification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscoveredLocation() {
        return discoveredLocation;
    }

    public void setDiscoveredLocation(String discoveredLocation) {
        this.discoveredLocation = discoveredLocation;
    }

    public int getDiscoveredByPersonId() {
        return discoveredByPersonId;
    }

    public void setDiscoveredByPersonId(int discoveredByPersonId) {
        this.discoveredByPersonId = discoveredByPersonId;
    }

    public Date getDiscoveredDate() {
        return discoveredDate;
    }

    public void setDiscoveredDate(Date discoveredDate) {
        this.discoveredDate = discoveredDate;
    }

    public int getEstimatedAge() {
        return estimatedAge;
    }

    public void setEstimatedAge(int estimatedAge) {
        this.estimatedAge = estimatedAge;
    }

    public Integer getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

}
