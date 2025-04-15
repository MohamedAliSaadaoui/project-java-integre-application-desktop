package GestionArticle.Entites;

import com.google.protobuf.TextFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Article {
    private int id;
    private String title;
    private Date date;
    private String content;
    private String image;
    private User user; // Association avec la classe User

    // ✅ Constructeur sans ID (pour insertion)
    public Article(String title, Date date, String content, String image) {
        this.title = title;
        this.date = date;
        this.content = content ;
        this.image = image;
        this.user = new User(1); // ID utilisateur par défaut
    }
    public Article(String title, Date date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;

        this.user = new User(1); // ID utilisateur par défaut
    }
    // ✅ Constructeur avec ID
    public Article(int id, String title, Date date, String content, String image, User user) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.image = image;
        this.user = user;
    }
    public Article(int id, String title, String content, String dateStr) {
        this.id = id;
        this.title = title;
        this.content = content;

        // Essayer de convertir la chaîne de date en objet Date
        try {
            this.date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }

        // Définir l'utilisateur par défaut, ou le personnaliser selon ton besoin
        this.user = new User(1); // ID utilisateur par défaut
    }
    public Article(int id, String title, Date date, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;


    }
    public Article(int id) {
        this.id = id;

    }
    public Article(String title, Date date, String content, String image, User user) {

        this.title = title;
        this.date = date;
        this.content = content;
        this.image = image;
        this.user = user;
    }



    // ✅ Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        if (date != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        return "";
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ✅ Equals et hashCode pour comparer les objets Article
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id == article.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ✅ toString pour debug/affichage
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", user=" + (user != null ? user.getId() : "null") +
                '}';
    }
}

