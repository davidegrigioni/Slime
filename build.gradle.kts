plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.3"
}

group = "cc.davyy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.hypera.dev/snapshots")
    maven("https://repo.panda-lang.org/releases")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:bcb0301fb1")

    // MiniMessage Library
    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    // Permission Library
    implementation("me.lucko.luckperms:minestom:5.4-SNAPSHOT")

    // Logging Library
    implementation("org.slf4j:slf4j-simple:2.0.16")

    // DI Library
    implementation("com.google.inject:guice:7.0.0")

    // Config Libraries
    implementation("ninja.leaping.configurate:configurate-hocon:3.7.1")
    implementation("dev.dejvokep:boosted-yaml:1.3.7")

    // Command Framework
    implementation("dev.rollczi:litecommands-minestom:3.8.0")

    // SQL Lite Implementation + ORM
    implementation("com.j256.ormlite:ormlite-jdbc:6.1")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "cc.davyy.slime.Slime"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate("me.lucko.luckperms", "cc.davyy.libs.luckperms")
        mergeServiceFiles()
        archiveClassifier.set("")
    }

}