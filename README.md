# Using MockWebServer to perform integrations tests in Android

In this article, I am going to demonstrate a correct way that I've found to preform integrations tests between retrofit and my code in Android development.

From this problem, I found the mockwebserver, a okhttp3 package, which serves to simulate a real server, and this approach served me well

To use it, you need to insert these packages in gradle:
```gradle
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

testImplementation 'com.squareup.okhttp3:mockwebserver:4.9.1'
```

As I was using clean code, I decided to start by creating the entity inside the domain folder. In this example I used a product object:
```kt
data class Product(
    val id: String,
    val name: String,
    val price: Float,
    val quantity: Int
)
```

I created a repository interface too, this is to know what I going test with mock wev server. Here I'll use the `getProducts` to test the GET method, the `createProduct` to test the POST method and the `deleteProduct` to test the DELETE method.
```kt
interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun createProduct(product: Product): Int
    suspend fun deleteProduct(product: Product): Boolean
}
```
