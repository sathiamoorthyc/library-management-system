package org.library.management.models.dto;

import java.io.Serializable;

public record BookDto(String iSBN, String title, String author, int publicationYear, int availableCopies) implements Serializable {
}
