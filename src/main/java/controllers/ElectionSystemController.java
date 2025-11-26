package controllers;

import algorithms.QuickSort;
import datastructures.DynamicArray;
import datastructures.HashTable;
import models.Candidate;
import models.Election;
import models.ElectionType;
import models.Politician;
import utils.Comparators;

import java.io.*;
import java.util.Comparator;

/**
 * Main Controller for the Elections Information System
 * Manages all politicians, elections, and candidates
 * Provides methods for CRUD operations, searching, filtering, and sorting
 */
public class ElectionSystemController {

    // Hash tables for fast lookup by unique identifiers
    private HashTable<String, Politician> politiciansByName;
    private HashTable<String, Election> electionsByID;

    // Additional storage for filtering/searching
    private DynamicArray<Politician> allPoliticians;
    private DynamicArray<Election> allElections;

    /**
     * Constructor - initializes data structures
     */
    public ElectionSystemController() {
        politiciansByName = new HashTable<>(101);
        electionsByID = new HashTable<>(101);
        allPoliticians = new DynamicArray<>();
        allElections = new DynamicArray<>();
    }

    // ==================== POLITICIAN OPERATIONS ====================

    /**
     * Add a new politician to the system
     *
     * @return true if added successfully, false if politician already exists
     */
    public boolean addPolitician(String name, String dateOfBirth, String politicalParty,
                                 String homeCounty, String imageUrl) {
        if (politiciansByName.containsKey(name)) {
            return false; // Politician already exists
        }

        Politician politician = new Politician(name, dateOfBirth, politicalParty, homeCounty, imageUrl);
        politiciansByName.put(name, politician);
        allPoliticians.add(politician);
        return true;
    }

    /**
     * Add an existing Politician object
     */
    public boolean addPolitician(Politician politician) {
        if (politician == null || politiciansByName.containsKey(politician.getName())) {
            return false;
        }
        politiciansByName.put(politician.getName(), politician);
        allPoliticians.add(politician);
        return true;
    }

    /**
     * Get politician by exact name (uses hashing for O(1) lookup)
     */
    public Politician getPolitician(String name) {
        return politiciansByName.get(name);
    }

    /**
     * Update politician information
     */
    public boolean updatePolitician(String originalName, String newName, String dateOfBirth,
                                    String politicalParty, String homeCounty, String imageUrl) {
        Politician politician = politiciansByName.get(originalName);
        if (politician == null) {
            return false;
        }

        // If name changed, update hash table key
        if (!originalName.equals(newName)) {
            politiciansByName.remove(originalName);
            politician.setName(newName);
            politiciansByName.put(newName, politician);
        }

        politician.setDateOfBirth(dateOfBirth);
        politician.setPoliticalParty(politicalParty);
        politician.setHomeCounty(homeCounty);
        politician.setImageUrl(imageUrl);
        return true;
    }

    /**
     * Delete politician (also removes from all elections)
     */
    public boolean deletePolitician(String name) {
        Politician politician = politiciansByName.get(name);
        if (politician == null) {
            return false;
        }

        // Remove from all elections
        for (int i = 0; i < allElections.size(); i++) {
            Election election = allElections.get(i);
            election.removeCandidate(politician);
        }

        // Remove from hash table and list
        politiciansByName.remove(name);
        allPoliticians.remove(politician);
        return true;
    }

    /**
     * Get all politicians
     */
    public DynamicArray<Politician> getAllPoliticians() {
        return allPoliticians;
    }

    // ==================== ELECTION OPERATIONS ====================

    /**
     * Add a new election to the system
     */
    public boolean addElection(ElectionType type, String location, String date, int numberOfSeats) {
        Election election = new Election(type, location, date, numberOfSeats);
        String electionId = election.getElectionId();

        if (electionsByID.containsKey(electionId)) {
            return false; // Election already exists
        }

        electionsByID.put(electionId, election);
        allElections.add(election);
        return true;
    }

    /**
     * Add existing Election object
     */
    public boolean addElection(Election election) {
        if (election == null || electionsByID.containsKey(election.getElectionId())) {
            return false;
        }
        electionsByID.put(election.getElectionId(), election);
        allElections.add(election);
        return true;
    }

    /**
     * Get election by ID (uses hashing for O(1) lookup)
     */
    public Election getElection(String electionId) {
        return electionsByID.get(electionId);
    }

    /**
     * Update election information
     */
    public boolean updateElection(String originalId, ElectionType type, String location,
                                  String date, int numberOfSeats) {
        Election election = electionsByID.get(originalId);
        if (election == null) {
            return false;
        }

        // Remove old ID
        electionsByID.remove(originalId);

        // Update election
        election.setType(type);
        election.setLocation(location);
        election.setDate(date);
        election.setNumberOfSeats(numberOfSeats);

        // Add with new ID (ID regenerates automatically)
        String newId = election.getElectionId();
        electionsByID.put(newId, election);
        return true;
    }

    /**
     * Delete election
     */
    public boolean deleteElection(String electionId) {
        Election election = electionsByID.get(electionId);
        if (election == null) {
            return false;
        }

        electionsByID.remove(electionId);
        allElections.remove(election);
        return true;
    }

    /**
     * Delete an election using an Election object (overloaded method).
     */
    public boolean deleteElection(Election election) {
        if (election == null) return false;

        // Remove from hash table
        electionsByID.remove(election.getElectionId());

        // Remove from array
        return allElections.remove(election);
    }


    /**
     * Get all elections
     */
    public DynamicArray<Election> getAllElections() {
        return allElections;
    }

    // ==================== CANDIDATE OPERATIONS ====================

    /**
     * Add candidate to an election
     *
     * @param politicianName Name of existing politician
     * @param electionId     ID of existing election
     * @param votes          Number of votes received
     * @param partyAtTime    Party affiliation at time of election
     */
    public boolean addCandidate(String politicianName, String electionId,
                                int votes, String partyAtTime) {
        Politician politician = politiciansByName.get(politicianName);
        Election election = electionsByID.get(electionId);

        if (politician == null || election == null) {
            return false; // Politician or election doesn't exist
        }

        // Check if already a candidate
        if (election.hasCandidate(politician)) {
            return false;
        }

        Candidate candidate = new Candidate(politician, election, votes, partyAtTime);
        election.addCandidate(candidate);
        return true;
    }

    /**
     * Update candidate votes
     */
    public boolean updateCandidateVotes(String politicianName, String electionId, int newVotes) {
        Politician politician = politiciansByName.get(politicianName);
        Election election = electionsByID.get(electionId);

        if (politician == null || election == null) {
            return false;
        }

        Candidate candidate = election.getCandidate(politician);
        if (candidate == null) {
            return false;
        }

        candidate.setVotes(newVotes);
        return true;
    }

    /**
     * Update candidate party affiliation at time of election
     */
    public boolean updateCandidateParty(String politicianName, String electionId, String newParty) {
        Politician politician = politiciansByName.get(politicianName);
        Election election = electionsByID.get(electionId);

        if (politician == null || election == null) {
            return false;
        }

        Candidate candidate = election.getCandidate(politician);
        if (candidate == null) {
            return false;
        }

        candidate.setPartyAtTime(newParty);
        return true;
    }

    /**
     * Remove candidate from election
     */
    public boolean removeCandidate(String politicianName, String electionId) {
        Politician politician = politiciansByName.get(politicianName);
        Election election = electionsByID.get(electionId);

        if (politician == null || election == null) {
            return false;
        }

        return election.removeCandidate(politician);
    }

    /**
     * Get all elections a politician has participated in
     */
    public DynamicArray<Election> getPoliticianElections(String politicianName) {
        Politician politician = politiciansByName.get(politicianName);
        if (politician == null) {
            return new DynamicArray<>();
        }

        DynamicArray<Election> participatedElections = new DynamicArray<>();
        for (int i = 0; i < allElections.size(); i++) {
            Election election = allElections.get(i);
            if (election.hasCandidate(politician)) {
                participatedElections.add(election);
            }
        }
        return participatedElections;
    }

    // ==================== SEARCH & FILTER OPERATIONS ====================

    /**
     * Search politicians by partial name (case-insensitive)
     */
    public DynamicArray<Politician> searchPoliticiansByName(String partialName) {
        return politiciansByName.searchByPartialKey(partialName);
    }

    /**
     * Simple search for elections by free text.
     * Uses hash table internally.
     * Matches by:
     * - electionId (через searchByPartialKey)
     * - location
     * - year
     */
    public DynamicArray<Election> searchElectionsSimple(String term) {
        DynamicArray<Election> results = new DynamicArray<>();

        if (term == null || term.trim().isEmpty()) {
            return electionsByID.values();
        }

        String search = term.toLowerCase().trim();

        DynamicArray<Election> byId = electionsByID.searchByPartialKey(term);
        for (int i = 0; i < byId.size(); i++) {
            results.add(byId.get(i));
        }

        DynamicArray<Election> all = electionsByID.values();
        for (int i = 0; i < all.size(); i++) {
            Election e = all.get(i);

            if (results.contains(e)) {
                continue;
            }

            if (e.getLocation().toLowerCase().contains(search) ||
                    e.getYear().toLowerCase().contains(search)) {
                results.add(e);
            }
        }

        return results;
    }


    /**
     * Filter politicians by party
     */
    public DynamicArray<Politician> filterPoliticiansByParty(String party) {
        DynamicArray<Politician> results = new DynamicArray<>();
        String searchParty = party.trim();

        for (int i = 0; i < allPoliticians.size(); i++) {
            Politician p = allPoliticians.get(i);
            if (p.getPoliticalParty().equalsIgnoreCase(searchParty)) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Filter politicians by county
     */
    public DynamicArray<Politician> filterPoliticiansByCounty(String county) {
        DynamicArray<Politician> results = new DynamicArray<>();
        String searchCounty = county.trim();

        for (int i = 0; i < allPoliticians.size(); i++) {
            Politician p = allPoliticians.get(i);
            if (p.getHomeCounty().equalsIgnoreCase(searchCounty)) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Multi-criteria politician search
     *
     * @param name   Partial name (null to ignore)
     * @param party  Party name (null to ignore)
     * @param county County name (null to ignore)
     */
    public DynamicArray<Politician> searchPoliticians(String name, String party, String county) {
        DynamicArray<Politician> results = new DynamicArray<>();

        for (int i = 0; i < allPoliticians.size(); i++) {
            Politician p = allPoliticians.get(i);
            boolean matches = true;

            // Check name if provided
            if (name != null && !name.trim().isEmpty()) {
                if (!p.getName().toLowerCase().contains(name.toLowerCase())) {
                    matches = false;
                }
            }

            // Check party if provided
            if (party != null && !party.trim().isEmpty()) {
                if (!p.getPoliticalParty().equalsIgnoreCase(party.trim())) {
                    matches = false;
                }
            }

            // Check county if provided
            if (county != null && !county.trim().isEmpty()) {
                if (!p.getHomeCounty().equalsIgnoreCase(county.trim())) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(p);
            }
        }
        return results;
    }

    /**
     * Filter elections by type
     */
    public DynamicArray<Election> filterElectionsByType(ElectionType type) {
        DynamicArray<Election> results = new DynamicArray<>();

        for (int i = 0; i < allElections.size(); i++) {
            Election e = allElections.get(i);
            if (e.getElectionType() == type) {
                results.add(e);
            }
        }
        return results;
    }

    /**
     * Filter elections by year
     */
    public DynamicArray<Election> filterElectionsByYear(String year) {
        DynamicArray<Election> results = new DynamicArray<>();

        for (int i = 0; i < allElections.size(); i++) {
            Election e = allElections.get(i);
            if (e.getYear().equals(year)) {
                results.add(e);
            }
        }
        return results;
    }

    /**
     * Multi-criteria election search
     *
     * @param type     Election type (null to ignore)
     * @param year     Year (null to ignore)
     * @param location Location (null to ignore)
     */
    public DynamicArray<Election> searchElections(ElectionType type, String year, String location) {
        DynamicArray<Election> results = new DynamicArray<>();

        for (int i = 0; i < allElections.size(); i++) {
            Election e = allElections.get(i);
            boolean matches = true;

            // Check type if provided
            if (type != null && e.getElectionType() != type) {
                matches = false;
            }

            // Check year if provided
            if (year != null && !year.trim().isEmpty()) {
                if (!e.getYear().equals(year.trim())) {
                    matches = false;
                }
            }

            // Check location if provided
            if (location != null && !location.trim().isEmpty()) {
                if (!e.getLocation().toLowerCase().contains(location.toLowerCase())) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(e);
            }
        }
        return results;
    }

    // ==================== SORTING OPERATIONS ====================

    /**
     * Sort politicians by specified criteria
     *
     * @param politicians Array to sort
     * @param sortBy      "name", "party", "county", "age"
     * @param ascending   Sort direction
     */
    public void sortPoliticians(DynamicArray<Politician> politicians, String sortBy, boolean ascending) {
        Comparator<Politician> comparator = Comparators.getPoliticianComparator(sortBy, ascending);
        QuickSort.sort(politicians, comparator);
    }

    /**
     * Sort elections by specified criteria
     *
     * @param elections Array to sort
     * @param sortBy    "year", "date", "type", "location", "seats"
     * @param ascending Sort direction
     */
    public void sortElections(DynamicArray<Election> elections, String sortBy, boolean ascending) {
        Comparator<Election> comparator = Comparators.getElectionComparator(sortBy, ascending);
        QuickSort.sort(elections, comparator);
    }

    /**
     * Sort candidates in an election by votes (descending by default)
     */
    public void sortCandidatesByVotes(Election election) {
        if (election == null) return;
        DynamicArray<Candidate> candidates = election.getCandidates();
        QuickSort.sort(candidates, Comparators.BY_VOTES);
    }

    /**
     * Sort candidates by custom criteria
     */
    public void sortCandidates(DynamicArray<Candidate> candidates, String sortBy, boolean ascending) {
        Comparator<Candidate> comparator = Comparators.getCandidateComparator(sortBy, ascending);
        QuickSort.sort(candidates, comparator);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Get total number of politicians in system
     */
    public int getPoliticianCount() {
        return allPoliticians.size();
    }

    /**
     * Get total number of elections in system
     */
    public int getElectionCount() {
        return allElections.size();
    }

    /**
     * Get all unique parties in the system
     */
    public DynamicArray<String> getAllParties() {
        DynamicArray<String> parties = new DynamicArray<>();

        for (int i = 0; i < allPoliticians.size(); i++) {
            String party = allPoliticians.get(i).getPoliticalParty();
            if (!parties.contains(party)) {
                parties.add(party);
            }
        }

        // Sort alphabetically
        QuickSort.sort(parties, (a, b) -> a.compareToIgnoreCase(b));
        return parties;
    }

    /**
     * Get all unique counties in the system
     */
    public DynamicArray<String> getAllCounties() {
        DynamicArray<String> counties = new DynamicArray<>();

        for (int i = 0; i < allPoliticians.size(); i++) {
            String county = allPoliticians.get(i).getHomeCounty();
            if (!counties.contains(county)) {
                counties.add(county);
            }
        }

        // Sort alphabetically
        QuickSort.sort(counties, (a, b) -> a.compareToIgnoreCase(b));
        return counties;
    }

    /**
     * Get all unique years from elections
     */
    public DynamicArray<String> getAllElectionYears() {
        DynamicArray<String> years = new DynamicArray<>();

        for (int i = 0; i < allElections.size(); i++) {
            String year = allElections.get(i).getYear();
            if (!years.contains(year)) {
                years.add(year);
            }
        }

        // Sort descending (most recent first)
        QuickSort.sort(years, (a, b) -> b.compareTo(a));
        return years;
    }

    /**
     * Clear all data from the system
     */
    public void clearAllData() {
        politiciansByName.clear();
        electionsByID.clear();
        allPoliticians.clear();
        allElections.clear();
    }


}
