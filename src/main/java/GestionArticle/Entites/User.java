package GestionArticle.Entites;


public class User {
    private int id;

    // Constructor
    public User(int id) {
        this.id = id;
    }

    // Getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{id=" + id + '}';
    }
}
