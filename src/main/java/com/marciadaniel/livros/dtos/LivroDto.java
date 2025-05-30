package com.marciadaniel.livros.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LivroDto(@NotBlank @NotNull String titulo,
                       @NotBlank @NotNull String autor,
                       @NotBlank
                       @Pattern(regexp = "(^$)|(978|979)-[0-9]+-[0-9]+-[0-9]+-[0-9]")
                       String isbn,
                       @NotNull
                       BigDecimal preco,
                        @Min(value = 0) int anoLancamento,
                       @NotBlank  String genero




                       ) {
}
