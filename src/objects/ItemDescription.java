package objects;

public class ItemDescription {

    private String discoveredDate;
    private int estimatedAge;
    private String type;
    private String material;
    private String metal;
    private Integer weight;
    private Integer valueEstimate;
    private String classification;
    private int diameter;
    private String museum;
    private String discovererName;
    private String discovererEmail;
    private String discovererPhoneNumber;

    public void setDiscoveredDate(String discoveredDate) {
        this.discoveredDate = discoveredDate;
    }

    public void setEstimatedAge(int estimatedAge) {
        this.estimatedAge = estimatedAge;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setValueEstimate(Integer valueEstimate) {
        this.valueEstimate = valueEstimate;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public void setMuseum(String museum) {
        this.museum = museum;
    }

    public void setDiscovererName(String discovererName) {
        this.discovererName = discovererName;
    }

    public void setDiscovererEmail(String discovererEmail) {
        this.discovererEmail = discovererEmail;
    }

    public void setDiscovererPhoneNumber(String discovererPhoneNumber) {
        this.discovererPhoneNumber = discovererPhoneNumber;
    }

    @Override
    public String toString(){

        String itemInfo = "";

        if(classification.equalsIgnoreCase("coin")){
            itemInfo = String.format(
                    "Mynte - %s | diameter: %s mm | %soppdaget %s og estimert til å være ca. %s år gammel. %s",
                    metal,
                    diameter,
                    museum == null ? "" : String.format("befinner seg på %s ", museum),
                    discoveredDate,
                    estimatedAge,
                    this.getDiscovererContact()
            );
        }

        if(classification.equalsIgnoreCase("jewelry")){
            itemInfo = String.format(
                    "Smykke - %s | oppdaget %s, estimert til å være %s år gammel med verdi estimat på %s NOK. %s",
                    type,
                    discoveredDate,
                    estimatedAge,
                    valueEstimate,
                    this.getDiscovererContact()
            );
        }


        if(classification.equalsIgnoreCase("weapon")){
            itemInfo = String.format(
                    "Våpen - %s | oppdaget %s våpen består av %s den veier ca. %s gram og estimert til å være %s år gammel. %s",
                    type,
                    discoveredDate,
                    material,
                    weight,
                    estimatedAge,
                    this.getDiscovererContact()
            );
        }

        return itemInfo;
    }

    private String getDiscovererContact(){
        return String.format(
                "Kontaktinfomasjon: %s tlf: %s epost: %s",
                discovererName,
                discovererPhoneNumber,
                discovererEmail
        );
    }
}
