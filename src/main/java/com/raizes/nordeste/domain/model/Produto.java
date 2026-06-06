package com.raizes.nordeste.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @Column(nullable = false, length = 20)
    private String temporada = "YEAR_ROUND";

    @Column(nullable = false)
    private Boolean disponivel = true;
}