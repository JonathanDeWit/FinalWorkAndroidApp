# Final Work Android App

This project is part of my Final Work project for my bachelor’s degree in applied computer science at the Erasmus Brussel University of Applied Sciences and Arts.
My project mainly consisted of building a video surveillance system by using microcontrollers.
If you want to know more about the project, I invite you to consult the following links:


## Purpose
This GitHub repo contains an Android app made in Kotlin.
The primary purpose of the application is to manage a video surveillance system. The user will have the possibility to turn the system on or off, monitor the system and watch live the video from the microcontrollers.
 
Every API call you will find in this project was directed to the ASP.Net API which you can find [here](https://github.com/JonathanDeWit/FinalWorkApi)


 ## Primary features
 - User Management
   - authenticate and store the JWT token
   - It's possible to create an account
   - Make all User CRUD operations
 - Security System management:
   - Turn the security system on or off
   - Monitor the state of the security system
   - Watch the live stream of every camera in the system
 
 
## Used technologies
 - [Exoplayer](https://exoplayer.dev/): As a player to request and play an SRT video stream.
   - I used the [Srtdroid](https://github.com/ThibaultBee/srtdroid) library to make Exoplayer compatible with the SRT protocol.
 - [Room](https://developer.android.com/training/data-storage/room): For Store locally the user's information’s on the device.
 - [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): I used Coroutines for executing API calls and database operation on a different thread.
 - [Volley](https://google.github.io/volley/): For making all the API calls
   - In addition to Volley I used the [Gson library](https://github.com/google/gson) to convert Json objects into Kotlin objects 
   
