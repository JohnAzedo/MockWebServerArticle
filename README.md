# Usando MockWebServer para fazer testes de integração no Android 

Nesse artigo, venho tentar demonstrar a maneira que encontrei para realizar testes de integração entre o retrofit e meu código no desenvolvimento Android.

Estava eu escrevendo meus testes unitários no projeto e me deparei com a possibilidade de escrever os testes do meu repositório (clean architecture) fazendo integração com o retrofit. A partir desse problema, encontrei o mockwebserver, um pacote do okhttp3, que serve para simular um servidor real, e essa abordagem me serviu perfeitamente.