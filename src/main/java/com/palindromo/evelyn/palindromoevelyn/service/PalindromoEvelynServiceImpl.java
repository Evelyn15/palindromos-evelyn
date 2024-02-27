package com.palindromo.evelyn.palindromoevelyn.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.palindromo.evelyn.palindromoevelyn.dto.ConsultaPalindromoResponseDTO;
import com.palindromo.evelyn.palindromoevelyn.dto.PalindromoResponseDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class PalindromoEvelynServiceImpl {

    static Integer quantidadePalindromos = 0;
    static String nomeArquivo = "";
    List<String> palindromosEncontrados = new ArrayList<>();

    /**
     * Processa o arquivo recebido em busca de palíndromos.
     * 
     * @param file O arquivo a ser processado.
     * @return O objeto ResponseEntity contendo a quantidade de palíndromos
     *         encontrados.
     */
    public PalindromoResponseDTO processarDadosPalindromo(MultipartFile file) {
        quantidadePalindromos = 0;
        palindromosEncontrados.clear();
        try {
            String conteudo = lerConteudoArquivo(file);
            nomeArquivo = file.getOriginalFilename();
            String[] linhas = conteudo.split("\\n");
            List<String> linhasSemEspacos = removerEspacos(linhas);
            char[][] matriz = gerarMatriz(linhasSemEspacos);
            encontrarPalindromos(matriz);
            return PalindromoResponseDTO.builder().qtdePalindromo(quantidadePalindromos)
                    .mensagem("Processado com Sucesso!").build();
        } catch (IOException e) {
            e.printStackTrace();
            return PalindromoResponseDTO.builder().qtdePalindromo(0).build();
        }
    }

    /**
     * Lê o conteúdo do arquivo recebido e retorna como uma string.
     * 
     * @param file O arquivo a ser lido.
     * @return A string contendo o conteúdo do arquivo.
     */
    public String lerConteudoArquivo(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader leitor = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder conteudo = new StringBuilder();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            conteudo.append(linha).append("\n");
        }
        leitor.close();
        return conteudo.toString();
    }

    /**
     * Remove os espaços em branco de cada linha do array de strings.
     * 
     * @param linhas O array de strings representando as linhas do arquivo.
     * @return Uma lista de strings sem espaços em branco.
     */
    public List<String> removerEspacos(String[] linhas) {
        List<String> linhasSemEspacos = new ArrayList<>();
        for (String linha : linhas) {
            String semEspacos = linha.replaceAll("\\s", "");
            linhasSemEspacos.add(semEspacos);
        }
        return linhasSemEspacos;
    }

    /**
     * Gera uma matriz de caracteres a partir da lista de strings sem espaços.
     * 
     * @param linhasSemEspacos A lista de strings sem espaços.
     * @return A matriz de caracteres gerada.
     */
    public char[][] gerarMatriz(List<String> linhasSemEspacos) {
        int numLinhas = linhasSemEspacos.size();
        int numColunas = linhasSemEspacos.get(0).length();
        char[][] matriz = new char[numLinhas][numColunas];
        for (int i = 0; i < numLinhas; i++) {
            matriz[i] = linhasSemEspacos.get(i).toCharArray();
        }
        return matriz;
    }

    /**
     * Encontra palíndromos na matriz nas direções horizontal, vertical e diagonal.
     * 
     * @param matriz A matriz de caracteres a ser verificada.
     */
    private void encontrarPalindromos(char[][] matriz) {
        int numLinhas = matriz.length;
        int numColunas = matriz[0].length;
        StringBuilder horizontal = new StringBuilder();
        StringBuilder vertical = new StringBuilder();
        StringBuilder diagonalPrincipal = new StringBuilder();
        StringBuilder diagonalAntiPrincipal = new StringBuilder();

        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                horizontal.append(matriz[i][j]);
                vertical.append(matriz[j][i]);
                if (i == j) {
                    diagonalPrincipal.append(matriz[i][j]);
                    diagonalAntiPrincipal.append(matriz[i][numColunas - 1 - j]);
                }
            }
            verificarPalindromo(horizontal.toString());
            verificarPalindromo(vertical.toString());
            horizontal.setLength(0);
            vertical.setLength(0);
        }
        verificarPalindromo(diagonalPrincipal.toString());
        verificarPalindromo(diagonalAntiPrincipal.toString());
        salvarPalindromosNoBanco(palindromosEncontrados);
    }

    /**
     * Verifica se uma string é um palíndromo.
     * 
     * @param str A string a ser verificada.
     */
    private void verificarPalindromo(String str) {
        StringBuilder reverso = new StringBuilder(str).reverse();
        StringBuilder palindromo = new StringBuilder();
        int esquerda = 0;
        int direita = str.length() - 1;
        String texto = "";

        while (esquerda < direita) {
            if (str.charAt(esquerda) != str.charAt(direita)) {
                esquerda++;
                texto = str.substring(esquerda, str.length());
            } else {
                palindromo.append(texto);
                break;
            }
        }

        if (palindromo.length() > 2) {
            String valor = "";
            valor = palindromo.toString();
            palindromo.setLength(0);
            quantidadePalindromos++;
            System.out.println(valor);
            palindromosEncontrados.add(valor);
        }
        if (str.equals(reverso.toString())) {
            quantidadePalindromos++;
            System.out.println(str);
            palindromosEncontrados.add(str);
        }

    }

    /**
     * Lê um arquivo do disco e retorna suas linhas como uma lista de strings.
     * 
     * @param caminhoArquivo O caminho do arquivo a ser lido.
     * @return Uma lista de strings contendo as linhas do arquivo.
     */
    static List<String> lerArquivo(String caminhoArquivo) {
        List<String> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    /**
     * Salva os palíndromos no banco de dados H2.
     * 
     * @param palindromos A lista de palíndromos a serem salvos.
     */
    private void salvarPalindromosNoBanco(List<String> palindromos) {
        String jdbcUrl = "jdbc:h2:" + Paths.get("src/main/java/com/palindromo/evelyn/palindromoevelyn/database")
                .toAbsolutePath(); // Caminho do banco de dados
        String username = "sa"; // Usuário padrão do H2
        String password = ""; // Senha padrão do H2

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
            Statement st = connection.createStatement();
            st.execute(
                    "CREATE TABLE IF NOT EXISTS PALINDROMOS (id INT PRIMARY KEY AUTO_INCREMENT, " +
                            "texto VARCHAR(255), " +
                            "data DATE DEFAULT CURRENT_DATE, " +
                            "arquivo VARCHAR(255))");
            for (String palindromo : palindromos) {
                String sql = "INSERT INTO PALINDROMOS (texto, arquivo) VALUES (?,?)";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, palindromo);
                    statement.setString(2, nomeArquivo);

                    statement.executeUpdate();
                }
            }
            st.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca os palíndromos salvos no banco de dados.
     * 
     * @return Uma lista de ConsultaPalindromoResponseDTO com os palíndromos
     *         encontrados.
     */
    public List<ConsultaPalindromoResponseDTO> buscarPalindromosNoBanco() {
        List<ConsultaPalindromoResponseDTO> listaDePalindromos = new ArrayList<>();
        String jdbcUrl = "jdbc:h2:" + Paths.get("src/main/java/com/palindromo/evelyn/palindromoevelyn/database")
                .toAbsolutePath(); // Caminho do banco de dados
        String username = "sa"; // Usuário padrão do H2
        String password = ""; // Senha padrão do H2

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement st = connection.createStatement();
            String sql = "SELECT * FROM PALINDROMOS";
            ResultSet resultSet = st.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String texto = resultSet.getString("texto");
                String data = resultSet.getString("data");
                String arquivo = resultSet.getString("arquivo");

                ConsultaPalindromoResponseDTO consultaResponse = new ConsultaPalindromoResponseDTO(
                        id, texto, data, arquivo);
                listaDePalindromos.add(consultaResponse);
            }

            resultSet.close();
            st.close();
            connection.close();

            return listaDePalindromos;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
