package com.controle.finansee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "despesas")
@Data // Anotação do Lombok que gera Getters, Setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos

public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;
}
