package utils;

import models.Politician;
import models.Election;
import models.Candidate;
import java.util.Comparator;

/**
 * Utility class containing various Comparator implementations
 * for sorting Politicians, Elections, and Candidates
 */
public class Comparators {

    // ==================== POLITICIAN COMPARATORS ====================

    /**
     * Compare politicians by name (ascending, case-insensitive)
     */
    public static final Comparator<Politician> BY_NAME = (p1, p2) ->
            p1.getName().compareToIgnoreCase(p2.getName());

    /**
     * Compare politicians by name (descending)
     */
    public static final Comparator<Politician> BY_NAME_DESC = (p1, p2) ->
            p2.getName().compareToIgnoreCase(p1.getName());

    /**
     * Compare politicians by party (ascending, Independents last)
     */
    public static final Comparator<Politician> BY_PARTY = (p1, p2) -> {
        // Put Independents at the end
        if (p1.isIndependent() && !p2.isIndependent()) return 1;
        if (!p1.isIndependent() && p2.isIndependent()) return -1;
        return p1.getPoliticalParty().compareToIgnoreCase(p2.getPoliticalParty());
    };

    /**
     * Compare politicians by party (descending)
     */
    public static final Comparator<Politician> BY_PARTY_DESC = (p1, p2) ->
            BY_PARTY.compare(p2, p1);

    /**
     * Compare politicians by county (ascending)
     */
    public static final Comparator<Politician> BY_COUNTY = (p1, p2) ->
            p1.getHomeCounty().compareToIgnoreCase(p2.getHomeCounty());

    /**
     * Compare politicians by county (descending)
     */
    public static final Comparator<Politician> BY_COUNTY_DESC = (p1, p2) ->
            p2.getHomeCounty().compareToIgnoreCase(p1.getHomeCounty());

    /**
     * Compare politicians by age (ascending)
     */
    public static final Comparator<Politician> BY_AGE = (p1, p2) ->
            Integer.compare(p1.getAge(), p2.getAge());

    /**
     * Compare politicians by age (descending)
     */
    public static final Comparator<Politician> BY_AGE_DESC = (p1, p2) ->
            Integer.compare(p2.getAge(), p1.getAge());

    /**
     * Compare politicians by date of birth (ascending - oldest first)
     */
    public static final Comparator<Politician> BY_DOB = (p1, p2) ->
            p1.getDateOfBirth().compareTo(p2.getDateOfBirth());

    /**
     * Compare politicians by date of birth (descending - youngest first)
     */
    public static final Comparator<Politician> BY_DOB_DESC = (p1, p2) ->
            p2.getDateOfBirth().compareTo(p1.getDateOfBirth());


    // ==================== ELECTION COMPARATORS ====================

    /**
     * Compare elections by year (ascending - oldest first)
     */
    public static final Comparator<Election> BY_YEAR = (e1, e2) ->
            e1.getYear().compareTo(e2.getYear());

    /**
     * Compare elections by year (descending - most recent first)
     */
    public static final Comparator<Election> BY_YEAR_DESC = (e1, e2) ->
            e2.getYear().compareTo(e1.getYear());

    /**
     * Compare elections by date (ascending - oldest first)
     */
    public static final Comparator<Election> BY_DATE = (e1, e2) ->
            e1.getDate().compareTo(e2.getDate());

    /**
     * Compare elections by date (descending - most recent first)
     */
    public static final Comparator<Election> BY_DATE_DESC = (e1, e2) ->
            e2.getDate().compareTo(e1.getDate());

    /**
     * Compare elections by type (alphabetical)
     */
    public static final Comparator<Election> BY_TYPE = (e1, e2) ->
            e1.getElectionType().name().compareTo(e2.getElectionType().name());

    /**
     * Compare elections by type (reverse alphabetical)
     */
    public static final Comparator<Election> BY_TYPE_DESC = (e1, e2) ->
            e2.getElectionType().name().compareTo(e1.getElectionType().name());

    /**
     * Compare elections by location (ascending)
     */
    public static final Comparator<Election> BY_LOCATION = (e1, e2) ->
            e1.getLocation().compareToIgnoreCase(e2.getLocation());

    /**
     * Compare elections by location (descending)
     */
    public static final Comparator<Election> BY_LOCATION_DESC = (e1, e2) ->
            e2.getLocation().compareToIgnoreCase(e1.getLocation());

    /**
     * Compare elections by number of seats (ascending)
     */
    public static final Comparator<Election> BY_SEATS = (e1, e2) ->
            Integer.compare(e1.getNumberOfSeats(), e2.getNumberOfSeats());

    /**
     * Compare elections by number of seats (descending)
     */
    public static final Comparator<Election> BY_SEATS_DESC = (e1, e2) ->
            Integer.compare(e2.getNumberOfSeats(), e1.getNumberOfSeats());

    /**
     * Compare elections by number of candidates (ascending)
     */
    public static final Comparator<Election> BY_CANDIDATES = (e1, e2) ->
            Integer.compare(e1.getNumberOfCandidates(), e2.getNumberOfCandidates());

    /**
     * Compare elections by number of candidates (descending)
     */
    public static final Comparator<Election> BY_CANDIDATES_DESC = (e1, e2) ->
            Integer.compare(e2.getNumberOfCandidates(), e1.getNumberOfCandidates());

    /**
     * Composite comparator: Type, then Year (descending)
     */
    public static final Comparator<Election> BY_TYPE_AND_YEAR = (e1, e2) -> {
        int typeCompare = BY_TYPE.compare(e1, e2);
        if (typeCompare != 0) return typeCompare;
        return BY_YEAR_DESC.compare(e1, e2);
    };


    // ==================== CANDIDATE COMPARATORS ====================

    /**
     * Compare candidates by votes (descending - highest first)
     * PRIMARY SORT FOR ELECTION RESULTS
     */
    public static final Comparator<Candidate> BY_VOTES = (c1, c2) ->
            Integer.compare(c2.getVotes(), c1.getVotes());

    /**
     * Compare candidates by votes (ascending - lowest first)
     */
    public static final Comparator<Candidate> BY_VOTES_ASC = (c1, c2) ->
            Integer.compare(c1.getVotes(), c2.getVotes());

    /**
     * Compare candidates by politician name (ascending)
     */
    public static final Comparator<Candidate> BY_CANDIDATE_NAME = (c1, c2) ->
            c1.getPoliticianName().compareToIgnoreCase(c2.getPoliticianName());

    /**
     * Compare candidates by politician name (descending)
     */
    public static final Comparator<Candidate> BY_CANDIDATE_NAME_DESC = (c1, c2) ->
            c2.getPoliticianName().compareToIgnoreCase(c1.getPoliticianName());

    /**
     * Compare candidates by party at time (ascending, Independents last)
     */
    public static final Comparator<Candidate> BY_PARTY_AT_TIME = (c1, c2) -> {
        // Put Independents at the end
        if (c1.wasIndependent() && !c2.wasIndependent()) return 1;
        if (!c1.wasIndependent() && c2.wasIndependent()) return -1;
        return c1.getPartyAtTime().compareToIgnoreCase(c2.getPartyAtTime());
    };

    /**
     * Compare candidates by party at time (descending)
     */
    public static final Comparator<Candidate> BY_PARTY_AT_TIME_DESC = (c1, c2) ->
            BY_PARTY_AT_TIME.compare(c2, c1);

    /**
     * Composite: Votes (desc), then Name (asc)
     * For displaying results with ties
     */
    public static final Comparator<Candidate> BY_VOTES_THEN_NAME = (c1, c2) -> {
        int voteCompare = BY_VOTES.compare(c1, c2);
        if (voteCompare != 0) return voteCompare;
        return BY_CANDIDATE_NAME.compare(c1, c2);
    };

    /**
     * Composite: Party, then Votes (desc)
     * For grouping candidates by party
     */
    public static final Comparator<Candidate> BY_PARTY_THEN_VOTES = (c1, c2) -> {
        int partyCompare = BY_PARTY_AT_TIME.compare(c1, c2);
        if (partyCompare != 0) return partyCompare;
        return BY_VOTES.compare(c1, c2);
    };


    // ==================== UTILITY METHODS ====================

    /**
     * Get politician comparator by name
     * @param sortBy "name", "party", "county", "age"
     * @param ascending Sort direction
     */
    public static Comparator<Politician> getPoliticianComparator(String sortBy, boolean ascending) {
        switch (sortBy.toLowerCase()) {
            case "name":
                return ascending ? BY_NAME : BY_NAME_DESC;
            case "party":
                return ascending ? BY_PARTY : BY_PARTY_DESC;
            case "county":
            case "location":
                return ascending ? BY_COUNTY : BY_COUNTY_DESC;
            case "age":
                return ascending ? BY_AGE : BY_AGE_DESC;
            case "dob":
            case "dateofbirth":
                return ascending ? BY_DOB : BY_DOB_DESC;
            default:
                return BY_NAME;
        }
    }

    /**
     * Get election comparator by name
     * @param sortBy "year", "date", "type", "location", "seats"
     * @param ascending Sort direction
     */
    public static Comparator<Election> getElectionComparator(String sortBy, boolean ascending) {
        switch (sortBy.toLowerCase()) {
            case "year":
                return ascending ? BY_YEAR : BY_YEAR_DESC;
            case "date":
                return ascending ? BY_DATE : BY_DATE_DESC;
            case "type":
                return ascending ? BY_TYPE : BY_TYPE_DESC;
            case "location":
                return ascending ? BY_LOCATION : BY_LOCATION_DESC;
            case "seats":
                return ascending ? BY_SEATS : BY_SEATS_DESC;
            case "candidates":
                return ascending ? BY_CANDIDATES : BY_CANDIDATES_DESC;
            default:
                return BY_YEAR_DESC;
        }
    }

    /**
     * Get candidate comparator by name
     * @param sortBy "votes", "name", "party"
     * @param ascending Sort direction
     */
    public static Comparator<Candidate> getCandidateComparator(String sortBy, boolean ascending) {
        switch (sortBy.toLowerCase()) {
            case "votes":
                return ascending ? BY_VOTES_ASC : BY_VOTES;
            case "name":
                return ascending ? BY_CANDIDATE_NAME : BY_CANDIDATE_NAME_DESC;
            case "party":
                return ascending ? BY_PARTY_AT_TIME : BY_PARTY_AT_TIME_DESC;
            default:
                return BY_VOTES;
        }
    }
}