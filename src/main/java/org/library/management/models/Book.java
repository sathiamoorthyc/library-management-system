package org.library.management.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Entity
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @NotNull(message = "ISBN of a book cannot be null")
    private String iSBN;
    @NotNull(message = "Title of a book cannot be null")
    private String title;
    @NotNull(message = "Author of a book cannot be null")
    private String author;
    private int publicationYear;
    private int availableCopies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(iSBN, book.iSBN);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(iSBN);
    }
}
