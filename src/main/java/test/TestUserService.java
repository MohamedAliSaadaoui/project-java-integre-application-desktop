package test;
import com.example.rewear.gestionuser.app.entities.User;
import com.example.rewear.gestionuser.app.services.UserService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TestUserService {
    public static void main(String[] args) {
        // Création d'une instance de UserService
        UserService userService = new UserService();

        try {
            // Test de la méthode recuperer() pour voir les utilisateurs existants
            System.out.println("Liste des utilisateurs avant ajout :");
            List<User> users = userService.recuperer();
            for (User user : users) {
                System.out.println(user);
            }

            // Test de la méthode ajouter()
            User newUser = new User();
            newUser.setUsername("testuser");
            newUser.setPassword("password123");
            newUser.setEmail("test@example.com");
            newUser.setNum_tel("1234567890");
            newUser.setDate_naiss(new Date());  // Date actuelle
            newUser.setAdresse("123 Test Street");

            System.out.println("\nAjout d'un nouvel utilisateur...");
            userService.ajouter(newUser);

            // Vérifier que l'utilisateur a bien été ajouté
            System.out.println("\nListe des utilisateurs après ajout :");
            users = userService.recuperer();
            for (User user : users) {
                System.out.println(user);
            }

            // Test de la recherche
            System.out.println("\nRecherche d'utilisateurs avec le mot-clé 'test' :");
            List<User> searchResults = userService.searchUsers("test");
            for (User user : searchResults) {
                System.out.println(user);
            }

            // Récupérer l'ID de l'utilisateur ajouté pour les tests suivants
            int userId = -1;
            for (User user : searchResults) {
                if (user.getUsername().equals("testuser")) {
                    userId = user.getId();
                    break;
                }
            }

            if (userId != -1) {
                // Test de la méthode getUserById()
                System.out.println("\nRécupération de l'utilisateur par ID :");
                User retrievedUser = userService.getUserById(userId);
                System.out.println(retrievedUser);

                // Test de la méthode modifier()
                retrievedUser.setUsername("modifieduser");
                System.out.println("\nModification de l'utilisateur...");
                userService.modifier(retrievedUser);

                // Vérifier que l'utilisateur a bien été modifié
                System.out.println("\nUtilisateur après modification :");
                retrievedUser = userService.getUserById(userId);
                System.out.println(retrievedUser);

                // Test de la méthode supprimer()
                System.out.println("\nSuppression de l'utilisateur...");
                userService.supprimer(retrievedUser);

                // Vérifier que l'utilisateur a bien été supprimé
                System.out.println("\nListe des utilisateurs après suppression :");
                users = userService.recuperer();
                for (User user : users) {
                    System.out.println(user);
                }
            }

            System.out.println("\nTests terminés avec succès !");

        } catch (SQLException e) {
            System.err.println("Erreur lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
