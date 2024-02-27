package com.palindromo.evelyn.palindromoevelyn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.palindromo.evelyn.palindromoevelyn.dto.ConsultaPalindromoResponseDTO;
import com.palindromo.evelyn.palindromoevelyn.dto.PalindromoResponseDTO;
import com.palindromo.evelyn.palindromoevelyn.service.PalindromoEvelynServiceImpl;

@RestController
@RequestMapping("/processar-palindromo")
public class PalindromoEvelynController {

    @Autowired
    private PalindromoEvelynServiceImpl service;

    @PostMapping("/")
    public ResponseEntity<PalindromoResponseDTO> processarPalindromo(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            PalindromoResponseDTO response = new PalindromoResponseDTO(0, "Arquivo Vazio");
            return ResponseEntity.badRequest().body(response);
        }
        PalindromoResponseDTO palindromoResponseDTO = service.processarDadosPalindromo(file);
        return ResponseEntity.ok(palindromoResponseDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ConsultaPalindromoResponseDTO>> buscarPalindromos() {
        List<ConsultaPalindromoResponseDTO> palindromos = service.buscarPalindromosNoBanco();
        return ResponseEntity.ok().body(palindromos);
    }
}