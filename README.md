[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<br />
<div align="center">
  <a href="https://github.com/ArmanKhanTech/MindMaster/">
    <img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/9e71f80e-5d98-4e77-950d-43fe322372e4" alt="Logo" width="80" height="80" >
  </a>

  <h3 align="center">MindMaster</h3>
  <p align="center">Status: Completed</p>
  <p align="center">An Android productivity app.</p>

  <p align="center">
    <a href="https://github.com/ArmanKhanTech/MindMaster"><strong>Explore the docs »</strong></a>
    <br />
    <a href="https://github.com/ArmanKhanTech/MindMaster/issues">Report a Bug</a>
    ·
    <a href="https://github.com/ArmanKhanTech/MindMaster/issues">Request new Feature</a>
  </p>
</div>
<br />



<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About the Project</a>
      <ul>
        <li><a href="#built-with">Built with</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#screenshots">Screenshots</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



## About the Project

MindMaster is an Android productivity app thats lets you do whats necessary by getting rid of all kinds of distractions.

**Supports Android 10 & above only.**

### Built with

* [![Java][Java]][Java-url]
* [![Kotlin][Kotlin]][Kotlin-url]
* [![Sqlite][Sqlite]][Sqlite-url]



## Getting Started

Follow the below instructions to get started.


### Prerequisites

<ol>
  <li>
    <p>Android Studio</p>
  </li>
  <li>
    <p>Java 8+</p>
  </li>
  <li>
    <p>Kotlin 1.9+</p>
  </li>
</ol>



### Installation

1. Clone this repository
   
   ```sh
   git clone https://github.com/ArmanKhanTech/MindMaster.git
   ```



## Usage

MindMaster offers a comprehensive set of features to help you manage your digital well-being and stay focused on your tasks. Here's an overview of its key functionalities:

**Blocking:**

<ol>
  <li><b>Apps & Games:</b> Block specific apps and games to prevent distractions and regain control over your time.</li>
  <li><b>Websites & Keywords:</b> Block unwanted websites and keywords to minimize exposure to harmful content or unproductive browsing.</li>
  <li><b>Notifications:</b> Silence notifications from distracting apps to maintain focus and avoid interruptions.</li>
</ol>
<br>

**Data Access Control:**

<ol>
  <li><b>Cellular & Wi-Fi:</b> Restrict cellular or Wi-Fi data access per app or game to reduce data usage and prevent unwanted background activity.</li>
</ol>
<br>

**Profiles & Schedules:**

<ol>
  <li><b>Multiple Profiles:</b> Create and manage different profiles for various use cases (e.g., work, study, leisure) with customized blocking settings.</li>
  <li><b>Flexible Schedules:</b> Set up to different blocking schedules to automate app and website blocking during specific times of the day or week.</li>
</ol>
<br>

**Usage Insights:**

<ol>
  <li><b>Detailed Overview:</b> Gain insights into your digital habits with comprehensive usage data for up to a year.</li>
  <li><b>Screen Time & Launch Count:</b> Track the amount of time spent on apps and the number of times you launch them.</li>
  <li><b>Data Usage:</b> Monitor data usage per app to identify areas for potential optimization.</li>
</ol>
<br>

**Additional Features:**

<ol>
  <li><b>Take-a-Break:</b> Temporarily block all distractions for a set duration to focus on other tasks or simply take a mindful break.</li>
  <li><b>Blocking Modes:</b> Choose from five blocking modes (Usage Limit, Specific Time Interval, Quick Block, Number of Launches & Fixed Block) to customize the intensity of your blocking experience.</li>
  <li><b>Normal & Strict Modes:</b> Switch between Normal and Strict modes to adjust the overall level of blocking and access restrictions.</li>
  <li><b>Uninstall Protection:</b> Prevent accidental uninstallation of MindMaster to ensure consistent protection against distractions.</li>
  <li><b>Password Protection:</b> Secure your settings and preferences with a password to maintain control over your blocking configurations.</li>
</ol>
<br>

**Supported Browsers:**

MindMaster currently supports blocking for the following popular browsers:

<ul>
  <li>Google Chrome</li>
  <li>Mozilla Firefox</li>
  <li>Opera</li>
  <li>Opera Mini</li>
</ul>

But more browsers can be added at `app/src/main/java/com/android/MindMaster/Service/LogURLService.java`
```java
private static List<SupportedBrowserConfig> getSupportedBrowsers() {
    List<SupportedBrowserConfig> browsers = new ArrayList<>();
    browsers.add(new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
    browsers.add(new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"));
    browsers.add(new SupportedBrowserConfig("com.opera.browser", "com.opera.browser:id/url_field"));
    browsers.add(new SupportedBrowserConfig("com.opera.mini.native", "com.opera.mini.native:id/url_field"));
    /*
    Syntax: browsers.add(new SupportedBrowserConfig("browser_package_name", "browser_package_name:id/url_field"));
    */
    return browsers;
}
```
& at `res/xml/accessibility_service.xml`
```html
<?xml version="1.0" encoding="utf-8"?>
<accessibility-service xmlns:android="http://schemas.android.com/apk/res/android"
    android:accessibilityEventTypes="typeAllMask"
    android:accessibilityFeedbackType="feedbackAllMask"
    android:accessibilityFlags="flagDefault|flagIncludeNotImportantViews|flagRequestTouchExplorationMode|flagRequestEnhancedWebAccessibility|flagReportViewIds|flagRetrieveInteractiveWindows"
    android:canRequestFilterKeyEvents="true"
    android:canRetrieveWindowContent="true"
    android:notificationTimeout="0"
    android:description="@string/accessibility_service_description"
    android:packageNames="com.android.chrome, org.mozilla.firefox, com.opera.browser, com.opera.mini.native", "here..." />
```
<br>


## Screenshots
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/d468a8a6-7af3-465a-87d7-39db13835af9" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/a3e82740-5f75-4de2-b5ac-efb7995ea54a" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/81c65e14-291c-4865-8ddb-2526aef89cd8" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/329648ee-b348-4e19-8d1e-4953d0906bbe" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/77eca1fe-f95a-4b0c-882c-2f46908c861b" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/a34dcbee-f772-4f0d-8846-7d9639b1bdc5" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/b6960e23-1e37-4517-8b6a-6e97c6c0a5f5" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/8a19428f-16fc-470d-b924-a54e3136eb32" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/30b41e95-035a-46af-95e0-eaeb8e5cbcfb" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/83347d7c-0253-48bb-a28e-7cb7cb1ecd36" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/78263931-b145-44b4-a17f-79fc90a39263" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/4470602f-73e1-49a6-88f2-87491b9b8868" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/123d02e1-d261-4929-b52f-ad50ce04fa32" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/3218b7c1-f122-4bcd-aafe-cfaf71f86c0a" alt="Screenshot" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/MindMaster/assets/92728787/67093edd-0dbf-40b0-b8c5-47cd3273c735" alt="Screenshot" width="250" height="500">



## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

Don't forget to give the project a star! 

Thanks again!



## License

Distributed under the MIT License. See `LICENSE.txt` for more information.



## Contact

Arman Khan - ak2341776@gmail.com

Project Link - [https://github.com/ArmanKhanTech/MindMaster](https://github.com/ArmanKhanTech/MindMaster)



## Acknowledgments


**3rd Party Libraries**
* [Phil Jay's MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [Lottie Animations](https://github.com/airbnb/lottie-android)
* [Hank's PasscodeView](https://github.com/hanks-zyh/PasscodeView)



[contributors-shield]: https://img.shields.io/github/contributors/ArmanKhanTech/MindMaster.svg?style=for-the-badge
[contributors-url]: https://github.com/ArmanKhanTech/MindMaster/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ArmanKhanTech/MindMaster.svg?style=for-the-badge
[forks-url]: https://github.com/ArmanKhanTech/MindMaster/network/members
[stars-shield]: https://img.shields.io/github/stars/ArmanKhanTech/MindMaster.svg?style=for-the-badge
[stars-url]: https://github.com/ArmanKhanTech/MindMaster/stargazers
[issues-shield]: https://img.shields.io/github/issues/ArmanKhanTech/MindMaster.svg?style=for-the-badge
[issues-url]: https://github.com/ArmanKhanTech/MindMaster/issues
[license-shield]: https://img.shields.io/github/license/ArmanKhanTech/MindMaster.svg?style=for-the-badge
[license-url]: https://github.com/ArmanKhanTech/MindMaster/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&Screenshot=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/arman-khan-25b624205/
[Android]: https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&Screenshot=android-studio&ScreenshotColor=white
[Android-url]: https://developer.android.com/
[Java]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&Screenshot=openjdk&ScreenshotColor=white
[Java-url]: https://www.java.com/
[Kotlin]: https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&Screenshot=kotlin&ScreenshotColor=white
[Kotlin-url]: https://kotlinlang.org/
[Gradle]: https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&Screenshot=Gradle&ScreenshotColor=white
[Gradle-url]: https://gradle.org/
[Sqlite]: https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&Screenshot=sqlite&ScreenshotColor=white
[Sqlite-url]: https://www.sqlite.org/
