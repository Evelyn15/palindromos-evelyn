package com.palindromo.evelyn.palindromoevelyn.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PalindromoResponseDTO {
    private Integer qtdePalindromo;
    private String mensagem;
}
