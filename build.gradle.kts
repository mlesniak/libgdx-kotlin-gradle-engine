plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    // id("org.graalvm.buildtools.native") version "0.9.12"
    application
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    val gdxVersion = "1.11.0"

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("com.mlesniak.engine.MainKt")
}


/*
if (OperatingSystem.current() == OperatingSystem.MAC_OS) {
        // Required to run on macOS
        jvmArgs += "-XstartOnFirstThread"
    }
 */