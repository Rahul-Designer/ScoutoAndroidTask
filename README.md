
# ScoutoAndroidTask

This app is completely based on REST API. With the help of REST API, we show the car details with the recycler view in the app layout and car details will store in a local database. Users can add the car image from the phone gallery and delete the car detail in the recycler view. And also integrate the Firebase for User authentication.

## Appendix

#### Why use Firebase Authentication

Firebase Authentication provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users to your app. It supports authentication using passwords, phone numbers, popular federated identity providers like Google, Facebook and Twitter, and more.


#### Why use Retrofit over HTTP

Retrofit is an easy and fast library to retrieve and upload data via a REST-based web service. Retrofit manages the process of receiving, sending, and creating HTTP requests and responses. It resolves issues before sending an error and crashing the application. It pools connections to reduce latency. It is used to cache responses to avoid sending duplicate requests.

#### Why use room over SQLite

* Compile-time verification of SQL queries. each @Query and @Entity is checked at the compile time, that preserves your app from crash issues at runtime and not only it checks the only syntax, but also missing tables.
* Boilerplate code
* Easily integrated with other Architecture components (like LiveData)
## API Reference

#### Get all vechicle manufacturer

```http
  GET https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `format` | `json` | |

#### Get all model name

```http
  GET https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMakeId/{makeId}?format=json
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `makeId`  | `string` | **Required**. makeId of vechicle list to fetch |
| `format`  | `json` | |



## Acknowledgements

 - [Firebase](https://firebase.google.com/docs/android/setup)
 - [Retrofit for GET API response ](https://www.topcoder.com/thrive/articles/retrofit-library-in-android)
 - [Room Library](https://developer.android.com/training/data-storage/room)

## Demo 

### Sample Apk File
[Sample Apk](https://github.com/Rahul-Designer/ScoutoAndroidTask/blob/master/ScoutoAndroidTask.apk)

### Demo Video
<img src="Sample_Video.mp4" width="40%">
