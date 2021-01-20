# uLesson App Assessment

## Introduction
The application is written entirely in Kotlin.

It uses the single activity acrhitecture pattern which is the recommended way of building modern android apps and it's entirely structured with
MVVM acrhitectire to ensure separation of concern and highly testable code.

With the help of [giuide to architecture](https://developer.android.com/jetpack/guide), we simply check if there's cached data, if there is, we display the cached data
and we make a background network call to update the database, otherwise (if the cache is empty), we make a network call, save the response to the local database and the UI is updated accordingly.

## Architecture
The app uses the dependency inversion rule to ensure separation of concerns and easy testing.
It consists of a single activity and 3 fragments

## Database structure
There are two tables in the database `subject` and `recentview`
subject is used to persist the subjects

`recentview` is used to persist the recently watched topics
accoridng to the UI, its worth noting that I had to use LIMIT to limit the records on table depending on whether the user clicks view all or less.

If the user clicks on view all I pass a 1000 number to the dao query to retrieve all the records, I'm not sure if thats a good idea, 
like what if we had a 1000 records, I think it can be done better like passing a dynamic query or something.
Also if the user clicks on view less, I pass 2 to the query.

## UI Structure
* DashboardFragment:
This fragment displays the list of subjects and recently watched videos, using room as source of data
Its worth noting that I had to use pngs for the topics inorder to have a beautiful and closer look to the UI, I would normally use the image response 
gotten from the endpoint.

* SubjectFragment: 
This fragment displays all the lessons of a particular subject clicked from the previous screen.
It requires an argument(``subjectId``) to be passed by the previous screen in order to get the lessons of the subject clicked.
No network call is made here, since the data is cached, we just query the room db to fetch the subject and update the UI accordingly.

* VideoPlayerFragment:
This fragment plays the video of a lesson, underneath is a title and subtitle TextView that shows a static dummy text, 
this is used just to have a closer look to the UI.
Something also worth noting is the ExoPlayer EventListener; when it receives the `ExoPlayer.STATE_READY` I assume that the video has been watched,
I made this decision inorder not to wait for the video to be completed before saving it to the `recentview` table, I think this should typically be done in the 
`ExoPlayer.STATE_ENDED`

Also the recent watched videos is saved using the `subjectId` `subjectName` and `chapterName` as topicName, so for example if I watched Standard deviation lesson
under a chapter called statistics in Mathemeatics subject, what would be saved in the `recentview` table is statistics as the `topicName`, Mathematics as the `subjectName`
and it uses the `subjectId` as the unique key so that when you watch another lesson in another chapter it updates the topic name as the most recently watched topic in that subject.

In other words, the recently wacthed videos is saved on a chapter level not an individual lesson.(Not sure if this is the best approach to that)

## Libraries used
* [Room](https://developer.android.com/topic/libraries/architecture/room): The Room persistence library provides an abstraction 
layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
* [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started): a library that manages and eases fragment transactions
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): for dependency injection
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata): An observable data holder class consumed by the layout to display ui data
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): a class that housed UI-related data
* [Retrofit](https://square.github.io/retrofit/): for making network request
* [Coroutine](https://developer.android.com/kotlin/coroutines): recommended way to make execute asynchronous code, main safe!, and eliminates call back hell code
* [Timber](): used for logging
* [Glide](https://github.com/bumptech/glide): a library for loading images
* [Exoplayer](https://exoplayer.dev/) : library used for playing videos

## Clone project
Use Android Studio 4.0 and above

## ToDo
* UI testing with espresso
* CI/CD

## License
```
MIT License

Copyright (c) 2020 Tobiloba Oyelekan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software isfurnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

