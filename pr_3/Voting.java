import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voting implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Candidate> candidates;
    private Map<User, Candidate> votes;

    public Voting(String title, String description, LocalDate startDate, LocalDate endDate) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.candidates = new ArrayList<>();
        this.votes = new HashMap<>();
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Candidate> getCandidates() { return candidates; }

    public boolean isActive() {
        return LocalDate.now().isBefore(endDate) || LocalDate.now().isEqual(endDate);
    }

    public boolean hasCandidate(Candidate candidate) {
        return candidates.contains(candidate);
    }

    public boolean hasUserVoted(User user) {
        return votes.containsKey(user);
    }

    public void addCandidate(Candidate candidate) {
        if (!candidates.contains(candidate)) {
            candidates.add(candidate);
        }
    }

    public void addVote(User user, Candidate candidate) {
        if (isActive() && candidates.contains(candidate)) {
            votes.put(user, candidate);
        }
    }

    @Override
    public String toString() {
        return "Voting{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", active=" + isActive() +
                '}';
    }
}
