Introduction
------------
Want to see information about the movie you are interested in? Welcome.
This application allows you to search for information about movies.

UI based on the [Dribble][1] project.

This is a test application that shows the skills of working with the libraries listed below.
It's app based on the MVVM architecture.

Libraries Used
--------------
* Foundation - Components for core system capabilities, Kotlin extensions and support for
  multidex and automated testing.
  * [AppCompat][2] - Degrade gracefully on older versions of Android.
* Architecture - A collection of libraries that help you design robust, testable, and
  maintainable apps. Start with classes for managing your UI component lifecycle and handling data
  persistence.
  * [Data Binding][11] - Declaratively bind observable data to UI elements.
  * [ViewModel][12] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
  * [ButterKnife][13] - Field and method binding for Android views.
  * [Shared Preferences][14] - Save key-value data.
* UI - Details on why and how to use UI Components in your apps - together or separate
  * [Fragment][21] - A basic unit of composable UI.
  * [Layout][22] - Lay out widgets using different algorithms. 
  * [Material Design][23] - Material design is a comprehensive guide for visual, motion, 
  and interaction design across platforms and devices. 
* Third party
  * [Palette][31] - The palette library is a support library that extracts prominent colors 
    from images to help you create visually engaging apps.
  * [Glide][32] - For image loading and for glide transformations.
  * [Glide Transformations][33] - An Android transformation library providing 
  a variety of image transformations for Glide.
  * [Picasso][34] - A powerful image downloading and caching library for Android.
  * [Retrofit][35] - A type-safe HTTP client for Android and Java.
  * [Gson][36] - A Java serialization/deserialization library to convert Java Objects 
  into JSON and back.
  
[1]: https://dribbble.com/shots/5088956-Cinema-Concept
[2]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat/
[11]: https://developer.android.com/topic/libraries/data-binding/
[12]: https://developer.android.com/topic/libraries/architecture/viewmodel/
[13]: https://jakewharton.github.io/butterknife/
[14]: https://developer.android.com/training/data-storage/shared-preferences
[21]: https://developer.android.com/guide/components/fragments/
[22]: https://developer.android.com/guide/topics/ui/declaring-layout/
[23]: https://developer.android.com/guide/topics/ui/look-and-feel/
[31]: https://developer.android.com/training/material/palette-colors/
[32]: https://bumptech.github.io/glide/
[33]: https://github.com/wasabeef/glide-transformations/
[34]: http://square.github.io/picasso/
[35]: https://square.github.io/retrofit/
[36]: https://github.com/google/gson/
