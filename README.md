[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<br />
<div align="center">
  <a href="https://github.com/ArmanKhanTech/Achievix/">
    <img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/b6eea048-0f67-4412-b3c4-63840778a990" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Achievix</h3>

  <p align="center">
    An Android productivity app.
    <br />
    <a href="https://github.com/ArmanKhanTech/Achievix"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/ArmanKhanTech/Achievix/issues">Report a Bug</a>
    ·
    <a href="https://github.com/ArmanKhanTech/Achievix/issues">Request new Feature</a>
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

Achievix is an Android productivity app thats lets you do whats necessary by getting rid of all kinds of distractions.

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
   git clone https://github.com/ArmanKhanTech/Achievix.git
   ```



## Usage

1. Block
<br>• Apps
<br>• Games
<br>• Websites
<br>• Keywords
<br>• Notifications
2. Block Data Access
<br>• Cellular, Wifi or Both
<br>• Per App or Game
4. Create and Manage Various Profiles
5. Add and Manage Five Different Blocking Schedules
6. View Usage
<br>• Upto a Year Old
<br>• Screen Time, Launch Count and Data Usage Wise
7. Take a Break Feature
<br>• Four Blocking Levels
8. Normal & Strict Mode
9. Uninstall Protection
10. Password Protection

Supported Browsers:
1. Google Chrome
2. Mozilla Firefox
3. Opera 
4. Opera Mini

More browsers can be added at `app/src/main/java/com/android/achievix/Service/LogURLService.java`
```java
private static List<SupportedBrowserConfig> getSupportedBrowsers() {
    List<SupportedBrowserConfig> browsers = new ArrayList<>();
    browsers.add(new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
    browsers.add(new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"));
    browsers.add(new SupportedBrowserConfig("com.opera.browser", "com.opera.browser:id/url_field"));
    browsers.add(new SupportedBrowserConfig("com.opera.mini.native", "com.opera.mini.native:id/url_field"));
    // here
    return browsers;
}
```



## Screenshots
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/5b34eaee-fba7-4258-a742-4cba52620b8b" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/a3e82740-5f75-4de2-b5ac-efb7995ea54a" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/81c65e14-291c-4865-8ddb-2526aef89cd8" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/329648ee-b348-4e19-8d1e-4953d0906bbe" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/77eca1fe-f95a-4b0c-882c-2f46908c861b" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/a34dcbee-f772-4f0d-8846-7d9639b1bdc5" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/8a19428f-16fc-470d-b924-a54e3136eb32" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/30b41e95-035a-46af-95e0-eaeb8e5cbcfb" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/83347d7c-0253-48bb-a28e-7cb7cb1ecd36" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/78263931-b145-44b4-a17f-79fc90a39263" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/4470602f-73e1-49a6-88f2-87491b9b8868" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/095e532c-e4b9-4330-82bb-d9feef312b76" alt="Logo" width="250" height="500">
<img src="https://github.com/ArmanKhanTech/Achievix/assets/92728787/88b5d183-5589-4464-af19-21a56a03ebf3" alt="Logo" width="250" height="500">



## Roadmap

- [x] Home Activity
- [x] App Block Activity
- [x] New Schedule Activity
- [x] Blocking Schedule Modes
- [x] Edit Schedule Activity
- [x] App Block Service
- [x] Website Block Activity
- [x] Keyword Block Activity
- [x] Web/Keyword Service
- [x] Internet Block Activity
- [x] Internet Block Service
- [x] Usage Overview Activity
- [x] App Insights Activity
- [x] Notification Blocker
- [x] Uninstall Protection
- [x] Take a Break Activity
- [x] Normal Mode
- [x] Strict Mode
- [x] Password Protection
- [x] App Launch Service
- [x] New Profile Activity
- [x] Edit Profile Activity

See the [open issues](https://github.com/ArmanKhanTech/Achievix/issues) for a full list of proposed features (and known issues)



## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

Don't forget to give the project a star! 

Thanks again!



## License

Distributed under the MIT License. See `LICENSE.txt` for more information.



## Contact

Arman Khan - ak2341776@gmail.com

Project Link - [https://github.com/ArmanKhanTech/Achievix](https://github.com/ArmanKhanTech/Achievix)



## Acknowledgments


**3rd Party Libraries**
* [Phil Jay's MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [Lottie Animations](https://github.com/airbnb/lottie-android)
* [Hank's PasscodeView](https://github.com/hanks-zyh/PasscodeView)



[contributors-shield]: https://img.shields.io/github/contributors/ArmanKhanTech/Achievix.svg?style=for-the-badge
[contributors-url]: https://github.com/ArmanKhanTech/Achievix/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ArmanKhanTech/Achievix.svg?style=for-the-badge
[forks-url]: https://github.com/ArmanKhanTech/Achievix/network/members
[stars-shield]: https://img.shields.io/github/stars/ArmanKhanTech/Achievix.svg?style=for-the-badge
[stars-url]: https://github.com/ArmanKhanTech/Achievix/stargazers
[issues-shield]: https://img.shields.io/github/issues/ArmanKhanTech/FocusOnMe.svg?style=for-the-badge
[issues-url]: https://github.com/ArmanKhanTech/Achievix/issues
[license-shield]: https://img.shields.io/github/license/ArmanKhanTech/FocusOnMe.svg?style=for-the-badge
[license-url]: https://github.com/ArmanKhanTech/Achievix/blob/master/LICENSE.txt
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
