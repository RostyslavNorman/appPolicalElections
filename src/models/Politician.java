package models;
import java.util.Objects;
import java.util.regex.Pattern;

public class Politician {
    private String name;
    private String dateOfBirth;
    private String politicalParty;
    private String homeCounty;
    private String imageUrl;
    //regex for YYYY-MM-DD format
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    public Politician(String name, String dateOfBirth, String politicalParty, String homeCounty, String imageUrl) {
        setName(name);
        setDateOfBirth(dateOfBirth);
        setPoliticalParty(politicalParty);
        setHomeCounty(homeCounty);
        setImageUrl(imageUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (Pattern.matches(DATE_PATTERN, dateOfBirth)){
            this.dateOfBirth = dateOfBirth;}
        else {
            throw new IllegalArgumentException("Invalid date format (YYYY-MM-DD)");
        }

    }

    public String getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(String politicalParty) {
        if (politicalParty == null || politicalParty.isEmpty()){
            this.politicalParty = "Independent";
        }else {
            this.politicalParty = politicalParty;
        }
    }

    public String getHomeCounty() {
        return homeCounty;
    }

    public void setHomeCounty(String homeCounty) {
        this.homeCounty = homeCounty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIndependent() {
        return "Independent".equalsIgnoreCase(politicalParty);
    }

// ========== EQUALS & HASHCODE ==========
    // Needed for hash table and comparisons

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Politician other = (Politician) obj;
        return name.equals(other.name) && dateOfBirth.equals(other.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + dateOfBirth.hashCode();
    }
}
