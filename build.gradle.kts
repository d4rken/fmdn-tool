buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$VERSION_DAGGER")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$VERSION_NAVIGATION")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean").configure {
    delete("build")
}