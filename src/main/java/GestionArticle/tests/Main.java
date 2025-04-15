package GestionArticle.tests;

import GestionArticle.Entites.Article;
import GestionArticle.Entites.Commentaire;
import GestionArticle.Entites.User;
import GestionArticle.Service.ServiceCommentaire;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Création d'une date SQL
        try {
            // Date actuelle
            Date sqlDate = new Date(new Date().getTime());

            // Création d'un utilisateur fictif avec un ID existant dans la DB
            User user = new User(1); // Remplace 1 par un ID valide

            // Création d'un article
            Article article = new Article(1,"lam",sqlDate,"adaz","ala",user);

            // Service
            //ServiceArticle sp = new ServiceArticle();

            // Ajouter un article
           // sp.modifier(article);

            // Supprimer un article (remplace 41 par un ID existant ou que tu viens d’ajouter)
      // sp.supprimer(1);

            // Afficher tous les articles
         //   System.out.println(sp.getAll());

        } catch (Exception e) {
            e.printStackTrace();
        }
        Date sqlDate = new Date(new Date().getTime());

        // Création d'un utilisateur fictif avec un ID existant dans la DB
        User user = new User(1); // Remplace 1 par un ID valide
        // Create the Article object with updated data
        Article article = new Article(1);
        article.setTitle("Updated Title");  // Set the updated title
        article.setContent("Updated Content");  // Set the updated content
        article.setImage("updated_image.jpg");  // Set the updated image

// Now create the Commentaire object with the updated article
        Commentaire commentaire = new Commentaire(1, user, article, sqlDate, "a");


        // Service
        ServiceCommentaire sp = new ServiceCommentaire();

        // Ajouter un article
     //  sp.ajouter(commentaire);
     //  sp.supprimer(1);
sp.modifier(commentaire);
        System.out.println(sp.getAll());
    }
}
