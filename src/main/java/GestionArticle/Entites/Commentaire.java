package GestionArticle.Entites;

import java.util.Date;

public class Commentaire {
    private int id;
    private User user;         // Association avec la classe User
    private Article article;   // Association avec la classe Article
    private Date date_comm;
    private String contenu_comm;
    public Commentaire(Article article, Date date_comm, String contenu_comm) {
        this.article = article;
        this.date_comm = date_comm;
        this.contenu_comm = contenu_comm;
    }
    // 🔹 Constructeur avec tous les champs (y compris ID)
    public Commentaire(int id, User user, Article article, Date date_comm, String contenu_comm) {
        this.id = id;
        this.user = user;
        this.article = article;
        this.date_comm = date_comm;
        this.contenu_comm = contenu_comm;
    }

    // 🔹 Constructeur sans ID (pour insertion)
    public Commentaire(User user, Article article, Date date_comm, String contenu_comm) {
        this.user = user;
        this.article = article;
        this.date_comm = date_comm;
        this.contenu_comm = contenu_comm;
    }

    // 🔹 Constructeur avec IDs uniquement (userId, articleId)
    public Commentaire(int userId, int articleId, Date date_comm, String contenu_comm) {
        this.user = new User(userId);
        this.article = new Article(null, null, null, null); // Création temporaire
        this.article.setId(articleId);
        this.date_comm = date_comm;
        this.contenu_comm = contenu_comm;
    }

    // 🔸 Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Date getDate_comm() {
        return date_comm;
    }

    public void setDate_comm(Date date_comm) {
        this.date_comm = date_comm;
    }

    public String getContenu_comm() {
        return contenu_comm;
    }

    public void setContenu_comm(String contenu_comm) {
        this.contenu_comm = contenu_comm;
    }

    // 🔹 toString personnalisé
    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                ", article=" + (article != null ? article.getTitle() : "null") +
                ", date_comm=" + date_comm +
                ", contenu_comm='" + contenu_comm + '\'' +
                '}';
    }
}
