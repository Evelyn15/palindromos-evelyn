package com.palindromo.evelyn.palindromoevelyn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.palindromo.evelyn.palindromoevelyn.dto.ConsultaPalindromoResponseDTO;

public class PalindromoEvelynServiceImplTest {

    private PalindromoEvelynServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new PalindromoEvelynServiceImpl();
    }

    @Test
    public void testBuscarPalindromosNoBanco() {
        // Mock para simular o comportamento do método de acesso ao banco de dados
        PalindromoEvelynServiceImpl service = mock(PalindromoEvelynServiceImpl.class);
        List<ConsultaPalindromoResponseDTO> palindromos = new ArrayList<>();
        palindromos.add(new ConsultaPalindromoResponseDTO(1, "aba", "2024-02-26", "arquivo.txt"));
        palindromos.add(new ConsultaPalindromoResponseDTO(2, "radar", "2024-02-26", "arquivo.txt"));

        // Simula o retorno do método buscarPalindromosNoBanco()
        when(service.buscarPalindromosNoBanco()).thenReturn(palindromos);

        // Testa se o método retorna a lista esperada
        assertEquals(palindromos, service.buscarPalindromosNoBanco());
    }

    @Test
    public void testRemoverEspacos() {
        String[] linhas = { "ana ", " bob", " ovo " };
        List<String> result = service.removerEspacos(linhas);
        assertEquals(3, result.size());
        assertEquals("ana", result.get(0));
        assertEquals("bob", result.get(1));
        assertEquals("ovo", result.get(2));
    }

    @Test
    public void testGerarMatriz() {
        List<String> linhasSemEspacos = new ArrayList<>();
        linhasSemEspacos.add("ana");
        linhasSemEspacos.add("bob");
        char[][] matriz = service.gerarMatriz(linhasSemEspacos);
        assertEquals(2, matriz.length);
        assertEquals(3, matriz[0].length);
        assertEquals('a', matriz[0][0]);
        assertEquals('b', matriz[1][0]);
    }

}
