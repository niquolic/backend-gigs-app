package com.example.backendgigsapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document("users")
public class UsersEntity {

    @Id
    private String id;

    // Index unique : filet de sécurité au niveau base de données contre les
    // logins en double, en plus de la vérification déjà faite dans
    // ServiceUser.register (qui, seule, a une petite fenêtre de course en
    // cas d'inscriptions strictement simultanées avec le même login).
    @Indexed(unique = true)
    private String login;

    private String password;

    // Le champ "gigs" (liste d'IDs) a été retiré : il n'était jamais lu
    // nulle part dans le code (getGigsByUserId interroge directement la
    // collection "gigs" par userId), seulement écrit lors des ajouts/
    // suppressions. Il ne servait à rien et pouvait se désynchroniser de
    // la réalité.
}
