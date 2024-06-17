[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<br />
<div align="center">
  <a href="https://github.com/ArmanKhanTech/AchieveIt/">
    <img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/b6eea048-0f67-4412-b3c4-63840778a990" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">AchieveIt </h3>

  <p align="center">
    An Android productivity app.
    <br />
    <a href="https://github.com/ArmanKhanTech/AchieveIt"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/ArmanKhanTech/AchieveIt/issues">Report a Bug</a>
    ·
    <a href="https://github.com/ArmanKhanTech/AchieveIt/issues">Request new Feature</a>
  </p>
</div>



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
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



## About the Project

AchieveIt is an Android productivity app thats lets you do whats necessary by getting rid of all kinds of distractions.

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
</ol>



### Installation

1. Clone this repository
   
   ```sh
   git clone https://github.com/ArmanKhanTech/AchieveIt.git
   ```



## Usage

AchieveIt offers a comprehensive set of features to help you manage your digital well-being and stay focused on your tasks. Here's an overview of its key functionalities:

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
  <li><b>Uninstall Protection:</b> Prevent accidental uninstallation of AchieveIt to ensure consistent protection against distractions.</li>
  <li><b>Password Protection:</b> Secure your settings and preferences with a password to maintain control over your blocking configurations.</li>
</ol>
<br>

**Supported Browsers:**

AchieveIt currently supports blocking for the following popular browsers:

<ul>
  <li>Google Chrome</li>
  <li>Mozilla Firefox</li>
  <li>Opera</li>
  <li>Opera Mini</li>
</ul>

But more browsers can be added at `app/src/main/java/com/android/AchieveIt/Service/LogURLService.java`
```java
private static List<SupportedBrowserConfig> getSupportedBrowsers() {
    List<SupportedBrowserConfig> browsers = new ArrayList<>();
    browsers.add(new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
    browsers.add(new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"));
    browsers.add(new SupportedBrowserConfig("com.opera.browser", "com.opera.browser:id/url_field"));
    browsers.add(new SupportedBrowserConfig("com.opera.mini.native", "com.opera.mini.native:id/url_field"));
    /*
    here...
    syntax: browsers.add(new SupportedBrowserConfig("browser_package_name", "browser_package_name:id/url_field"));
    */
    return browsers;
}
```
<br>


## Screenshots
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/5b34eaee-fba7-4258-a742-4cba52620b8b" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/a3e82740-5f75-4de2-b5ac-efb7995ea54a" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/81c65e14-291c-4865-8ddb-2526aef89cd8" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/329648ee-b348-4e19-8d1e-4953d0906bbe" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/77eca1fe-f95a-4b0c-882c-2f46908c861b" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/a34dcbee-f772-4f0d-8846-7d9639b1bdc5" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/8a19428f-16fc-470d-b924-a54e3136eb32" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/30b41e95-035a-46af-95e0-eaeb8e5cbcfb" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/83347d7c-0253-48bb-a28e-7cb7cb1ecd36" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/78263931-b145-44b4-a17f-79fc90a39263" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/4470602f-73e1-49a6-88f2-87491b9b8868" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/095e532c-e4b9-4330-82bb-d9feef312b76" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/AchieveIt/assets/92728787/88b5d183-5589-4464-af19-21a56a03ebf3" alt="Logo" width="250" height="500">



## Roadmap

- [x] Home Activity
- [x] App Block Activity
- [x] New Schedule Activity
- [x] Blocking Schedule Modes
- [x] Edit Schedule Activity
- [x] App Block Service
- [x] Website Block Activity
- [x] Keyword Block Activity
- [x] Web/Keyword Block Service
- [x] Internet Block Activity
- [x] Internet Block Service
- [x] Usage Overview Activity
- [x] App Insights Activity
- [x] Notification Block Service
- [x] Uninstall Protection
- [x] Take-a-Break Activity
- [x] Normal Mode
- [x] Strict Mode
- [x] Password Protection
- [x] App Launch Service
- [x] New Profile Activity
- [x] Edit Profile Activity

See the [open issues](https://github.com/ArmanKhanTech/AchieveIt/issues) for a full list of proposed features (and known issues)



## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

Don't forget to give the project a star! 

Thanks again!



## License

Distributed under the MIT License. See `LICENSE.txt` for more information.



## Contact

Arman Khan - ak2341776@gmail.com

Project Link - [https://github.com/ArmanKhanTech/AchieveIt](https://github.com/ArmanKhanTech/AchieveIt)



## Acknowledgments


**3rd Party Libraries**
* [Phil Jay's MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [Lottie Animations](https://github.com/airbnb/lottie-android)
* [Hank's PasscodeView](https://github.com/hanks-zyh/PasscodeView)



[contributors-shield]: https://img.shields.io/github/contributors/ArmanKhanTech/AchieveIt.svg?style=for-the-badge
[contributors-url]: https://github.com/ArmanKhanTech/AchieveIt/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ArmanKhanTech/AchieveIt.svg?style=for-the-badge
[forks-url]: https://github.com/ArmanKhanTech/AchieveIt/network/members
[stars-shield]: https://img.shields.io/github/stars/ArmanKhanTech/AchieveIt.svg?style=for-the-badge
[stars-url]: https://github.com/ArmanKhanTech/AchieveIt/stargazers
[issues-shield]: https://img.shields.io/github/issues/ArmanKhanTech/FocusOnMe.svg?style=for-the-badge
[issues-url]: https://github.com/ArmanKhanTech/AchieveIt/issues
[license-shield]: https://img.shields.io/github/license/ArmanKhanTech/FocusOnMe.svg?style=for-the-badge
[license-url]: https://github.com/ArmanKhanTech/AchieveIt/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/arman-khan-25b624205/
[Android]: https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white
[Android-url]: https://developer.android.com/
[Java]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/
[Kotlin]: https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Gradle]: https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white
[Gradle-url]: https://gradle.org/
[Sqlite]: https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white
[Sqlite-url]: https://www.sqlite.org/
