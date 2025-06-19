import java.io.Serializable;

public class Candidate implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private User user;
    private String info;
    private String bio;
    private String program;
    private String contacts;

    public Candidate(User user, String info) {
        this.id = nextId++;
        this.user = user;
        this.info = info;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public String getContacts() { return contacts; }
    public void setContacts(String contacts) { this.contacts = contacts; }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", user=" + user.getFullName() +
                ", info='" + info + '\'' +
                '}';
    }
}
