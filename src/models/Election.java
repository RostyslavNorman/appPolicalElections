package models;

import datastructures.DynamicArray;
import java.util.regex.Pattern;

public class Election {

    private String electionId;
    private ElectionType type;
    private String location;
    private String date;
    private int numberOfSeats;
    private DynamicArray<Candidate> candidates;
    //regex for YYYY-MM-DD format
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    public Election(ElectionType type, String location, String date, int numberOfSeats) {
        if (type == null) {
            throw new IllegalArgumentException("Election type cannot be null");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        if (numberOfSeats < 1) {
            throw new IllegalArgumentException("Number of seats must be at least 1");
        }

        this.type = type;
        this.location = location.trim();
        setDate(date);
        this.numberOfSeats = numberOfSeats;
        this.candidates = new DynamicArray<>();
        this.electionId = generateElectionId();
    }
    /**
     * Generate unique election ID
     * Format: TYPE-LOCATION-YEAR
     * Example: "GENERAL-DUBLIN_CENTRAL-2020"
     */
    private String generateElectionId() {
        String year = getYear();
        String locationPart = location.toUpperCase().replaceAll("\\s+", "_");
        return type.name() + "-" + locationPart + "-" + year;
    }


    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public ElectionType getElectionType() {
        return type;
    }

    public void setType(ElectionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
        this.electionId = generateElectionId(); //regenerate ID
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        this.location = location;
        this.electionId = generateElectionId();

    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        if (date == null || date.length() < 4) return "0000";
        return date.substring(0, 4);
    }

    public void setDate(String date) {
        if (Pattern.matches(DATE_PATTERN, date)) {
            this.date = date;
        }else {
            throw new IllegalArgumentException("Invalid date");
        }
        this.electionId = generateElectionId();
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        if (numberOfSeats < 1) {
            throw new IllegalArgumentException("Number of seats cannot be less than 0");
        }
        this.numberOfSeats = numberOfSeats;
    }

    public DynamicArray<Candidate> getCandidates() {
        return candidates;
    }

    public int getNumberOfCandidates() {
        return candidates.size();
    }

    public void setCandidates(DynamicArray<Candidate> candidates) {
        this.candidates = candidates;
    }

    /**
     * Add candidate to this election
     */
    public void addCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }

        // Check if politician already a candidate
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getPolitician().equals(candidate.getPolitician())) {
                throw new IllegalArgumentException("Politician already a candidate");
            }
        }

        candidates.add(candidate);
    }

    /**
     * Remove candidate from election
     */
    public boolean removeCandidate(Politician politician) {
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getPolitician().equals(politician)) {
                candidates.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Get candidate by politician
     */
    public Candidate getCandidate(Politician politician) {
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getPolitician().equals(politician)) {
                return candidates.get(i);
            }
        }
        return null;
    }

    /**
     * Check if politician is a candidate
     */
    public boolean hasCandidate(Politician politician) {
        return getCandidate(politician) != null;
    }

    /**
     * Check if candidate is a winner (based on position and seats)
     * Assumes candidates are sorted by votes
     */
    public boolean isWinner(Candidate candidate) {
        for (int i = 0; i < numberOfSeats && i < candidates.size(); i++) {
            if (candidates.get(i).equals(candidate)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Election other = (Election) obj;
        return electionId.equals(other.electionId);
    }

    @Override
    public int hashCode() {
        return electionId.hashCode();
    }



}
