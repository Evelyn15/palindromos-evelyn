# Projeto Micro Serviço de Palíndromos
Este é um micro serviço desenvolvido em Java que oferece dois endpoints para atender às seguintes demandas:

1. Encontrar Palíndromos em uma Matriz Quadrada
Endpoint: POST /processar-palindromo/

Este endpoint recebe um arquivo TXT com uma matriz quadrada, com no máximo 10 colunas e 10 linhas. O serviço encontra todos os palíndromos existentes nessa matriz, considerando as posições horizontal, vertical e diagonal em qualquer direção. As palavras encontradas são persistidas no banco de dados.

Exemplo de Resposta:
{
  "quantidade_palindromos": 3
}

2. Consultar Registros no Banco de Dados
Endpoint: GET /processar-palindromo/buscar

Este endpoint consulta os registros no banco de dados, agrupando por arquivo enviado e data/hora.
