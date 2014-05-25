KiiLib android volley
========

This is the Android library for Kii Cloud with volley implementation. This library depends on the following libraries. 
* volley
* KiiLib-Java

How to add this library on your project
==========
1. Clone

    git clone https://github.com/fkmhrk/kiilib-android-volley.git

2. Copy m2repository folder to your project

    AppProject
      - app
        - build.gradle
      - m2repository
      - build.gradle

3. Add the following entries to your app/build.gradle

    repositories {
        maven {
            url "../m2repository"
        }
    }

    dependencies {
        compile 'jp.fkmsoft.libs:KiiLib-Android-Volley:2.+'
        compile fileTree(dir: 'libs', include: ['*.jar'])
        // add other dependencies here
    }
    
4. Click "Sync Project with Gradle Files" on Android Studio.