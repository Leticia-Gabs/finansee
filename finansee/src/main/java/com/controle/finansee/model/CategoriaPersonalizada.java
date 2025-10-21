package com.controle.finansee.model;

import com.controle.finansee.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias_personalizadas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome", "usuario_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaPersonalizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique foi removido pois impediria que diferentes usuarios criassem as mesmas categorias
    @Column(nullable = false)
    private String nome;

    // novo campo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCategoria tipo;

    //novo campo
    @Column(nullable = false)
    private String cor;

    // vinculo com o usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

}

