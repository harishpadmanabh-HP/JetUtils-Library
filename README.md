# JetUtils-Library
Library providing the starter utils for projects

### Country code dialog
#### Here we included a composable for country code dialog with flag emojis.
![dialog](https://user-images.githubusercontent.com/87296765/215941540-121fd819-bc6e-445b-80df-0df488460210.gif)
> Loading Dialog composable
  ```
  CountryCodeDialog(onCountrySelected = { country -> 
                
   })
  ```

### Permission dialog
#### Here we included a composable for permission dialog.
> Loading Permission dialog composable
  ```
  PermissionDialog(permissions = arrayListOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CAMERA
    ), onPermissionNotAvailable = { deniedPermissions ->
    // permission denied.
    }, onPermissionNotGranted = { deniedPermissions, permissionLauncherState ->
    // permission not granted.
    }, onAllPermissionsEnabled = {
    // when all permissions enabled.    
    }) 
  ```

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
            response.ifFailed { uiText ->
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
