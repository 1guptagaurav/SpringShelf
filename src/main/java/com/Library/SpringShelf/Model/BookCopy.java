package com.Library.SpringShelf.Model;


import jakarta.persistence.*;

@Entity
@Table(name="book_copies")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Book book;

    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus;

    @Column(nullable = false)
    private String barcode;
}
