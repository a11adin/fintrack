package ru.renatrenat.fintrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    private Account account;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "description", length = 1023)
    private String description;

}