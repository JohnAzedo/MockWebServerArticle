# Using MockWebServer to perform integrations tests in Android

In this article, I am going to demonstrate a correct way that I've found to preform integrations
tests between retrofit and my code in Android development.

From this problem, I found the mockwebserver, a okhttp3 package, which serves to simulate a real
server, and this approach served me well

To use it, you need to insert these packages in gradle:

```gradle
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.9.1'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2'
```

As I was using clean code, I decided to start by creating the entity inside the domain folder. In
this example I used a product object:

```kt
    data class Product(
        val id: String,
        val name: String,
        val price: Float,
        val quantity: Int
    )
```

I created a repository interface too, this is to know what I'm going to test with mock wev server.
Here I'll use the `getProducts` to test the GET method, the `createProduct` to test the POST method
and the `deleteProduct` to test the DELETE method.

```kt
    interface ProductRepository {
        suspend fun getProducts(): List<Product>
    }
```

Now we can go to infra package to start to implement the ProductRepository.

First things first, I created the responses files to convert json to a data class, realize
that `toProduct` method was used to convert `ProductResponse` to the product entity.

```kt
    data class ProductListResponse (
        @SerializedName("results")
        val products: List<ProductResponse>
    )
    
    data class ProductResponse(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Float,
        @SerializedName("quantity")
        val quantity: Int
    ){
        fun toProduct() = Product(
            id = id,
            name = name,
            price = price,
            quantity = quantity
        )
    }
```

After that I created the service interface that will be used by Retrofit to make a request to
server. In this file pay attention to the imports, `GET` and `Response` need be packages from
retrofit

```kt
    import retrofit2.Response
    import retrofit2.http.GET
    
    interface ProductService {
        @GET("products")
        suspend fun getProducts(): Response<ProductListResponse>
    }
```

Then I implemented the `ProductRepository` to a ProductRepositoryImpl class. This is a simple
implementation to get response and transform this in a products array:

```kt
    class ProductRepositoryImpl(
        private val service: ProductService
    ): ProductRepository{
    
        override suspend fun getProducts(): List<Product> {
            val response = service.getProducts().body()
            val products = response?.products ?: listOf()
            return products.map {
                it.toProduct()
            }
        }
    }
```

Now with the initial code created let's move on to the important part of this article, the creation
of tests.

To start this part we must create a mock json to simulate a server response, for this example, we
can use a array of products. This json file must be created in the tests folder inside a resource
folder:

```json
    {
  "results": [
    {
      "id": "3644",
      "name": "Galaxy S10 plus",
      "price": 3000.0,
      "quantity": 20
    },
    {
      "id": "3464",
      "name": "Galaxy S22 plus",
      "price": 8000.0,
      "quantity": 40
    },
    {
      "id": "7236",
      "name": "IPhone 13",
      "price": 5000.0,
      "quantity": 50
    },
    {
      "id": "3851",
      "name": "IPhone 8 plus",
      "price": 2000.0,
      "quantity": 10
    },
    {
      "id": "7364",
      "name": "Nokia 1100",
      "price": 100.0,
      "quantity": 5
    }
  ]
}
```

After that, create a class in a test folder with this structure:

```kt
    class ProductRepositoryImplIntegrationTest {
        private lateinit var repository: ProductRepository
        private lateinit var server: MockWebServer
    
        @Before
        fun setup(){
            // ...
        }
    
        @After
        fun stopServer(){
            server.shutdown()
        }
    }
```

We can see a variable to our repository and one for the MockWebServer, also we see two
methods, `setup` to start the server and config the retrofit and `stopServer` that will stop the
server after each test.

In setup function, we must start a MockWebServer instance and configure the Retrofit like in a real
situation. The next step is create the server and inject it in the repository:

````kt
    @Before
    fun setup(){
        server = MockWebServer()
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("products/"))
            .addConverterFactory(gson).build()
        val service: ProductService = retrofit.create(ProductService::class.java)
        repository = ProductRepositoryImpl(service)
    }
````

Before we create the tests, we must create a function to receive the filename and read our JSON file
to get the data. This same function will also create a mock response and put the data in the response body.

````kt
    private fun enqueueResponse(filename: String, headers: Map<String, String> = emptyMap()) {
        val path = "src/test/java/com/example/mockwebserverexample/resources/$filename"
        val file = File(path).readText(Charsets.UTF_8)
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        server.enqueue(mockResponse.setBody(file))
    }
````