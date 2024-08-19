package ru.maxima.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idPerson;

    @NotEmpty(message = "Name should not not be empty")
    @Size(min = 2, max = 50, message = "Name should be min 2 symbols and max 50 symbols")
    @Column(name = "name")
    private String name;
    @Min(value = 0, message = "age should be min 1900 years")
    @Column(name = "age")
    private Integer age;
    @OneToMany(mappedBy = "ownerOfBook", fetch = FetchType.EAGER)
    private List<Book> books;
    @NotEmpty(message = "Поле электронной почты не может быть пустым")
    @Column(name = "email")
    private String email;
    @NotEmpty(message = "Поле номера телефона не может быть пустым")
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotEmpty(message = "Поле пароля не может быть пустым")
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "removed_at")
    private LocalDateTime removedAt;
    @Column(name = "created_person")
    private String createdPerson;
    @Column(name = "removed_person")
    private String removedPerson;
    @Column(name = "removed")
    private Boolean removed;


    @Override
    public String toString() {
        return "Person{" +
                "idPerson=" + idPerson +
                ", fullName='" + name + '\'' +
                ", yearOfBirth=" + age +
                '}';
    }
}
