# Usando MockWebServer para fazer testes de integração no Android

Nesse artigo, venho tentar demonstrar a maneira que encontrei para realizar testes de integração entre o retrofit e meu código no desenvolvimento Android.


A partir desse problema, encontrei o mockwebserver, um pacote do okhttp3, que serve para simular um servidor real, e essa abordagem me serviu perfeitamente.

Para utiliza-lo é necessário inserir esses três pacotes:
```gradle
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

testImplementation 'com.squareup.okhttp3:mockwebserver:4.9.1'
```

Como estava utilizando clean code, decidi começar pela criação da entidade dentro da pasta de domain. Nesse exemplo utilizei um objeto de produto:
```kt
data class Product(
    val id: String,
    val name: String,
    val price: Float,
    val quantity: Int
)
```
Também criei a interface de um repósitório para saber o que vai ser testado com o mock web server, aqui vou utilizar o `getProducts` para testar o método GET, o `createProduct` para testarmos o método POST e o 'deleteProduct' para ver como se comporta o método DELETE

```kt
interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun createProduct(product: Product): Int
    suspend fun deleteProduct(product: Product): Boolean
}
```