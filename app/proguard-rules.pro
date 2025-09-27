# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/user/tools/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If you use reflection, typically to load external classes, you might need
# to keep them.
#-keep public class com.example.MyClass
#-keepclassmembers class com.example.MyClass {
#   public <init>();
#}

# If you use native libraries, you might want to keep the JNI classes.
#-keepclasseswithmembernames class * {
#    native <methods>;
#}