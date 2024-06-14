This application was created during The Essentials of Industry-Level Android App Development course.

The Running App is an offline-first Android application designed to track and store user runs locally and remotely. Developed using Kotlin and Jetpack Compose, the app leverages modern Android development practices to deliver a robust and seamless user experience. It utilizes a Service to track location in the background, ensuring that user runs are accurately monitored even when the app is not in the foreground. Additionally, the Google Maps API is integrated to display the current run, providing a visual representation of the route taken.

Key Features:

Jetpack Compose: The app utilizes Jetpack Compose to create a seamless and responsive UI, allowing for a modern and intuitive user interface.

Room Database Integration: Runs are stored locally in the Room database, providing efficient data management. Users can delete stored runs. The app ensures data consistency by clearing and caching remote runs in response to user changes.

Ktor: The app uses Ktor for asynchronous network operations, enabling the app to post, delete, and retrieve runs from a remote API efficiently.

Koin: Koin is used for dependency injection, promoting a clean and maintainable codebase by managing dependencies effectively.

Offline-first Capability: The app is designed to function smoothly even without an internet connection or when the device is in airplane mode. It stores finished runs locally and employs Work Managers to sync these runs in the background once the internet connection is restored.

Background Tracking: The app uses a Service to track the user's run in the background, ensuring continuous tracking without interruption. The current run duration is displayed as a notification, providing real-time feedback to the user.

Analytics Dynamic Feature: The app includes a Runs Analytics feature, implemented as a Dynamic Feature, allowing users to access detailed analytics about their runs. This feature can be dynamically installed, optimizing app performance and storage.

Multimodal Approach: The app is designed with a multimodal approach, ensuring easy accommodating of additional features and functionalities. This makes the app versatile and ready for future expansions, whether integrating new tracking capabilities, adding different types of workouts, or incorporating additional health metrics.

This comprehensive approach to development ensures that the Running App not only meets the immediate user's needs but also provides a scalable and maintainable solution for long-term use. Its design allows for easy expansion, making it a future-proof choice for users seeking a reliable and adaptable running companion.

Screenshots:

Saved runs overview:

![Saved runs](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/4eea2a74-3cf6-44b2-bea4-c830348881cb)

Active run screen:

![active run](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/0b58425a-d882-4d04-9631-5d58a43ef631)

Notification:

![active run notification](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/293fb2f3-703d-472b-b979-58338773f0c2)

Analytics screen:

![Analytics](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/87a33da1-9ba3-4c0e-8f27-daf34a912823)

Log in screen:

![Log in](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/e020a49d-79ae-4ff8-869f-6e7fdf629a5b)

Sing up screen:

![Sign up](https://github.com/IgorPetrovKrsk/RunningApp/assets/105622148/baf9e8b2-6edb-479a-a475-030b2d96f3d6)
