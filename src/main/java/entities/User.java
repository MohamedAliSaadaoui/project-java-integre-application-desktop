package entities;
import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String photo;
    private String num_tel;
    private Date date_naiss;
    private String adresse;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec ID
    public User(int id) {
        this.id = id;
    }

    // Constructeur avec champs essentiels
    public User(String username, String password, String email, String num_tel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.num_tel = num_tel;
    }

    // Constructeur complet
    public User(int id, String username, String password, String email,
                String photo, String num_tel, Date date_naiss, String adresse) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo = photo;
        this.num_tel = num_tel;
        this.date_naiss = date_naiss;
        this.adresse = adresse;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }

    public Date getDate_naiss() {
        return date_naiss;
    }

    public void setDate_naiss(Date date_naiss) {
        this.date_naiss = date_naiss;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", num_tel='" + num_tel + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
