package ru.maxima.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idBook;
    @NotEmpty(message = "Name should not not be empty")
    @Size(min = 2, max = 50, message = "Name should be min 2 symbols and max 50 symbols")
    private String name;
    @Size(min = 2, max = 50, message = "Name author should be min 2 symbols and max 50 symbols")
    private String author;
    @ManyToOne
    @JoinColumn(name = "p_id", referencedColumnName = "id")
    private Person ownerOfBook;
    @Min(value = 1900,message = "Год издания книги должен быть больше 1900 года")
    @Column(name = "year_of_production")
    private Integer yearOfProduction;
    @NotEmpty(message = "Имя автора не может быть пустым")
    @NotEmpty(message = "Поле описание не может быть пустым")
    @Size(min = 2,message = "Поле описание не может быть менее 2 символов")
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "removed_at")
    private LocalDateTime removedAt;
    @Column(name = "removed")
    private Boolean removed;
    @Column(name = "created_person")
    private String createdPerson;
    @Column(name = "updated_person")
    private String updatedPerson;
    @Column(name = "removed_person")
    private String removedPerson;




}
