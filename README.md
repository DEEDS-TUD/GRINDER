# GRINDER

## Building GRINDER

Due to the latest repository re-structuring, Android FI specific Java code was
moved to another repository. Some minor build dependencies with the Android
code remain to be resolved. These dependencies are introduced by GRINDER's
current static CUE Abstraction registry mechanism, which will be changed in the
future. Until the issue is properly resolved, GRINDER can still be built as
long as all dependencies are available in the local Maven repository.

In order to make all dependencies available in the local Maven repository,
repeated invocations of the `mvn install` command in the right order may be
needed.


### Example for Android FI Code

The following Maven invocations usually yield a successful GRINDER build:

```
android_fi/GRINDER $ mvn clean install
android_fi/grinder-android $ mvn clean install
android_fi/GRINDER $ mvn install
```


