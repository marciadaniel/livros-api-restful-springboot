package com.marciadaniel.livros.dtos;

import jakarta.validation.constraints.Pattern;

public record LoginRequest(String username, String password) {
}
