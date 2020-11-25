# Current Release
![Release](https://jitpack.io/v/amrafridi29/inline-activity-result.svg)

(https://jitpack.io/#amrafridi29/inline-activity-result/Tag)

# inline-activity-result

A kotlin library to simplify retrofit calls.



# Gradle

```
dependencies {
    ...
    implementation 'com.github.amrafridi29:inline-activity-result:latestversion'
}

allprojects {
    repositories {
       ...
      maven { url 'https://jitpack.io' }
     }
   }
```

# How to use 

```
askFile(FileType.IMAGES){resultData->
    //okResult here
 }.onCancel {resultData->
     //ask it again
    //resultData.askAgain()
  }.onPermissionDenied {resultData->
   if(resultData.hasDenied())
        resultData.askAgainPermission()
    if(resultData.hasForeverDenied()) 
      resultData.goToSettings()
}

```
