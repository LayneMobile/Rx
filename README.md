# RxSubscriptions
Rx add-ons for Android

## Download
```groovy
repositories {
    maven { url 'http://dl.bintray.com/laynemobile/rx' }
}

dependencies {
    // Rx Subscriptions
    compile 'com.laynemobile.rx:subscriptions:0.1.0'

    // Rx Subscriptions components
    compile 'com.laynemobile.rx:subscriptions-components:0.1.0'
    
    // Rx Requests
    compile 'com.laynemobile.rx:requests:0.1.0'
    
    // Rx Retrofit Requests
    compile 'com.laynemobile.rx:requests-retrofit:0.1.0'
}
```
## Build

To build:

```bash
$ git clone git@github.com:LayneMobile/Rx.git
$ cd Rx/
$ ./gradlew build
```

[![Build Status](https://travis-ci.org/LayneMobile/Rx.svg?branch=master)](https://travis-ci.org/LayneMobile/RxSubscriptions/builds)
