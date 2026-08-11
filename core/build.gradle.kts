plugins {
    java
    id("java-library")
//    id("com.gradleup.shadow") version "9.6.1"
//    id("xyz.jpenilla.run-paper") version "3.1.0"

}

//repositories {
//    mavenCentral()
//    maven("https://repo.papermc.io/repository/maven-public/")
//}

dependencies {
//    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    api("net.kyori:adventure-api:5.2.0")
    api("net.kyori:adventure-text-serializer-legacy:5.2.0")
    api("net.kyori:adventure-text-minimessage:4.17.0")
}

//java {
//    toolchain.languageVersion = JavaLanguageVersion.of(21)
//}
//
//tasks {
//    build {
//        dependsOn(shadowJar)
//    }
//
//    runServer {
//        // Configure the Minecraft version for our task.
//        // This is the only required configuration besides applying the plugin.
//        // Your plugin's jar (or shadowJar if present) will be used automatically.
//        minecraftVersion("1.21.11")
//        jvmArgs("-Xms2G", "-Xmx2G")
//    }
//
//    processResources {
//        val props = mapOf("version" to version)
//        filesMatching("plugin.yml") {
//            expand(props)
//        }
//    }
//}
repositories {
    mavenCentral()
}
