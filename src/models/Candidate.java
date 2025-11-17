package models;

public class Candidate {
    private Politician politician;
    private Election election;
    private int votes;
    private String partyAtTime;


    public Candidate(Politician politician, Election election, int votes, String partyAtTime) {
        setVotes(votes);
        setPolitician(politician);
        setElection(election);
        setPartyAtTime(partyAtTime);
    }
    public Politician getPolitician() {
        return politician;
    }

    public void setPolitician(Politician politician) {
        if (politician == null) {
            throw new IllegalArgumentException("Politician cannot be null");
        }
        this.politician = politician;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        if (votes < 0) {
            throw new IllegalArgumentException("Votes cannot be negative");
        }
        this.votes = votes;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        if (election == null) {
            throw new IllegalArgumentException("Election cannot be null");
        }
        this.election = election;
    }

    public String getPartyAtTime() {
        return partyAtTime;
    }

    public void setPartyAtTime(String partyAtTime) {
        this.partyAtTime = (partyAtTime != null && !partyAtTime.isEmpty()) ? partyAtTime.trim() : "Independent";
    }

    public boolean wasIndependent(){
        return "Independent".equalsIgnoreCase(partyAtTime);
    }

    public double getVotePercentage(int totalVotes) {
        if (totalVotes <= 0) {
            return 0.0;
        }
        return (votes *100.0)/ totalVotes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Candidate other = (Candidate) obj;
        return politician.equals(other.politician) && election.equals(other.election);
    }

    @Override
    public int hashCode() {
        return politician.hashCode() + election.hashCode();
    }
}
