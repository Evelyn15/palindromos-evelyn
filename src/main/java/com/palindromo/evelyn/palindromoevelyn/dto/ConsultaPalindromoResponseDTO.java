package com.palindromo.evelyn.palindromoevelyn.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ConsultaPalindromoResponseDTO {
    private Integer id;
    private String palindromo;
    private String data;
    private String arquivo;
}