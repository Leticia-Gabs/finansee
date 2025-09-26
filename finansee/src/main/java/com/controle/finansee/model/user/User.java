package com.controle.finansee.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// representa uma entidade:
@Entity
// representa uma tabela no BD, logo:
@Table(name = "users")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String password;
}
