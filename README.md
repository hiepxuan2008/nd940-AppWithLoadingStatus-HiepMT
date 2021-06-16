# Design an App with Application Loading Status Project

## Description

#### Project Overview
In this project students will create an app to download a file from Internet by clicking on a custom-built button where:
 - width of the button gets animated from left to right
 - text gets changed based on different states of the button
 - circle gets be animated from 0 to 360 degrees

A notification will be sent once the download is complete. When a user clicks on notification, the user lands on detail activity and the notification gets dismissed. In detail activity, the status of the download will be displayed and animated via MotionLayout upon opening the activity.

[The final look of the app](https://www.youtube.com/watch?v=a2l2cuMWh20)

#### Project Steps
```
1. Create a radio list of the following options where one of them can be selected for downloading:
- https://github.com/bumptech/glide
- https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter
- https://github.com/square/retrofit
2. Create a custom loading button by extending View class and assigning custom attributes to it
3. Animate properties of the custom button once it’s clicked
4. Add the custom button to the main screen, set on click listener and call download() function with selected Url
5. If there is no selected option, display a Toast to let the user know to select one.
6. Once the download is complete, send a notification with custom style and design
7. Add a button with action to the notification, that opens a detailed screen of a downloaded repository
8. Create the details screen and display the name of the repository and status of the download
9. Use declarative XML with motionLayout to coordinate animations across the views on the detail screen
10. Add a button to the detail screen to return back to the main screen.

```

#### Bonus requirement
```
1. Handle the animation if downloading takes a longer or shorter time than animation in the custom button 
[We don’t know how fast is the download, so once it’s complete make a function that cancels current animation and starts it over with different duration]
2. Add an additional view (EditText) in MainActivity where users can enter custom URLs for downloading/uploading files 
[Make sure to check if the inputted value is a valid url]
3. Open the downloaded file and display the information to the user on DetailsActivity
4. Customize notification UI based on the status of the download/upload (progress, fail, success)
```

#### Technical skills
- Using Notifications
- Create Custom Views
- Draw canvas objects
- Android Property Animations
- Using MotionLayout to Animate Android Apps

## My Demo

https://user-images.githubusercontent.com/6292433/122275106-86eaee80-cf0d-11eb-8fbb-eef0a4c36c35.mp4



## How to build the app

Follow below steps:

```
1. Clone this repository
2. Open `starter` folder via Android Studio IDE
3. Click on "Run" button in Android Studio to install the project on the phone or emulator
```

## License
Mai Thanh Hiep & Udacity under [Android Kotlin Developer NanoDegree](https://www.udacity.com/course/android-kotlin-developer-nanodegree--nd940) course.
