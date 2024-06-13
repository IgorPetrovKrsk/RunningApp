This application was created during The Essentials of Industry-Level Android App Development course.

The  Running App is an offline-first Android application that tracks and stores user runs locally and  remotely. Developed using Kotlin and Jetpack compose. 

Key Features:

Jetpack Compose: The app uses jetpack compose for seamless UI.

Room DB integration: The app stores runs in the local Room database. Users can delete stored runs. The local database clears and caches remote runs in case of user changes. 

Ktor:  The app uses Ktor to asynchronously post, delete and get runs from remote API.

Koin: The app utilizes Koin for dependency injection.

Offline-first: If the app can't save the current run (absence of internet or airplane mode), the app stores finished runs locally, then uses Work Managers to sync runs in the background.

Background tracking: The app uses Service to track user run in the background and to show user's current run duration as the notification.

Analytics Dynamic Feature: Runs Analytics was designed and implemented as the Dynamic Feature.

Screenshots:

![Saved runs](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/4eea2a74-3cf6-44b2-bea4-c830348881cb)

![active run](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/0b58425a-d882-4d04-9631-5d58a43ef631)

![active run notification](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/293fb2f3-703d-472b-b979-58338773f0c2)

![Analytics](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/87a33da1-9ba3-4c0e-8f27-daf34a912823)

![Log in](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/e020a49d-79ae-4ff8-869f-6e7fdf629a5b)

![Sign up](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/baf9e8b2-6edb-479a-a475-030b2d96f3d6)
