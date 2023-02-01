# JetUtils-Library
Library providing the starter utils for projects

### Network Extension
#### Here we included a network extension for api calls.
> Perform Api call
  ```
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient
                    .Builder().readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .build()
            )
            .build()

        val retrofitApi = retrofit.create(ApiInterface::class.java)
        var response: NetworkResult<FreeApiModel> //FreeApiModel -> sample response model
        lifecycleScope.launch {
            response = tryApiCall {
                ApiResult.Success(retrofitApi.getAllData())
            }

            response.ifSuccess { freeApiModel ->
                // do the operations after api success 
            }
            response.ifFailed { code, message ->
                // do the operations on api failed 
            }
        }
  }

interface ApiInterface {
    @GET("entries")
    suspend fun getAllData(): FreeApiModel
}
  ``` 
- NetworkResult<T> --> Generic class which accepts all the types of data
- NetworkResult.Success(T) --> This class will return the response model
- NetworkResult.Failure(UiText.DynamicString(exception.message)) --> This class will return the exception
  - NetworkResult<T>.doIfSuccess {} : This will be executed if the api call is success (code: 200)
  - NetworkResult<T>.doIfFailure {} : This will be executed if the api call is failed
