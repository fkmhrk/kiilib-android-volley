KiiLib android volley
========

This is the library for Kii Cloud Android with volley implementation. This library depends on the following libraries. 
* volley
* KiiLib-Java

So please install them on your local maven repository

How to install on your local maven repository
==========
install gradle 1.10 to build volley. 
----
go to http://www.gradle.org/

download gradle-1.10-bin.zip and unzip

add bin to PATH

install volley on your local maven repository
----
clone volley from here https://android.googlesource.com/platform/frameworks/volley/

add the following section to build.gradle in volley

    apply plugin: 'maven'
    uploadArchives {
        repositories.mavenDeployer {
            repository url: "file://$System.env.HOME/.m2/repository"
            pom.version = '1.0.0'
            pom.groupId = 'com.google'
            pom.artifactId = 'volley'
        }
    } 

execute the following command

    $  gradle uploadArchives
    :compileLint
    :copyReleaseLint UP-TO-DATE
    :mergeReleaseProguardFiles
    :packageReleaseAidl UP-TO-DATE
    :preBuild
    :preReleaseBuild
    :checkReleaseManifest
    :prepareReleaseDependencies
    :compileReleaseAidl
    :compileReleaseRenderscript
    :generateReleaseBuildConfig
    :mergeReleaseAssets
    :generateReleaseResValues
    :generateReleaseResources
    :packageReleaseResources
    :processReleaseManifest
    :processReleaseResources
    :generateReleaseSources
    :compileReleaseJava
    :processReleaseJavaRes UP-TO-DATE
    :packageReleaseJar
    :compileReleaseNdk
    :packageReleaseJniLibs UP-TO-DATE
    :packageReleaseLocalJar UP-TO-DATE
    :packageReleaseRenderscript UP-TO-DATE
    :bundleRelease
    :uploadArchives
    Uploading: com/google/volley/1.0.0/volley-1.0.0.aar to repository remote at file:///<your home>/.m2/repository
    Transferring 78K from remote
    Uploaded 78K
    
    BUILD SUCCESSFUL
    
    Total time: 8.315 secs

install KiiLib-Java on your local maven repository
----
Please see https://github.com/fkmhrk/KiiLib-Java/

install this library on your local maven repository
----
    $ sh gradlew uploadArchives
    Relying on packaging to define the extension of the main artifact has been deprecated and is scheduled to be removed in Gradle 2.0
    :kiilib:compileLint
    :kiilib:copyReleaseLint UP-TO-DATE
    :kiilib:mergeReleaseProguardFiles UP-TO-DATE
    :kiilib:packageReleaseAidl UP-TO-DATE
    :kiilib:preBuild
    :kiilib:preReleaseBuild
    :kiilib:checkReleaseManifest
    :kiilib:preDebugBuild
    :kiilib:preDebugTestBuild
    :kiilib:prepareComGoogleVolley100Library UP-TO-DATE
    :kiilib:prepareReleaseDependencies
    :kiilib:compileReleaseAidl UP-TO-DATE
    :kiilib:compileReleaseRenderscript UP-TO-DATE
    :kiilib:generateReleaseBuildConfig UP-TO-DATE
    :kiilib:mergeReleaseAssets UP-TO-DATE
    :kiilib:generateReleaseResValues UP-TO-DATE
    :kiilib:generateReleaseResources UP-TO-DATE
    :kiilib:mergeReleaseResources UP-TO-DATE
    :kiilib:processReleaseManifest UP-TO-DATE
    :kiilib:processReleaseResources UP-TO-DATE
    :kiilib:generateReleaseSources UP-TO-DATE
    :kiilib:compileReleaseJava UP-TO-DATE
    :kiilib:processReleaseJavaRes UP-TO-DATE
    :kiilib:packageReleaseJar UP-TO-DATE
    :kiilib:compileReleaseNdk UP-TO-DATE
    :kiilib:packageReleaseJniLibs UP-TO-DATE
    :kiilib:packageReleaseLocalJar UP-TO-DATE
    :kiilib:packageReleaseRenderscript UP-TO-DATE
    :kiilib:packageReleaseResources UP-TO-DATE
    :kiilib:bundleRelease UP-TO-DATE
    :kiilib:uploadArchives
    Uploading: jp/fkmsoft/libs/KiiLib-Android-Volley/2.0.0/KiiLib-Android-Volley-2.0.0.aar to repository remote at file:///<your home>/.m2/repository
    Transferring 27K from remote
    Uploaded 27K
    
    BUILD SUCCESSFUL
    
    Total time: 7.533 secs

