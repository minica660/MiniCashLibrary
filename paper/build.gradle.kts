plugins {
//    id("java-library")
//    id("com.gradleup.shadow") version "9.6.1"

    id("com.gradleup.shadow")
//    id("xyz.jpenilla.run-paper")
//    id("xyz.jpenilla.run-paper") version "3.1.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":core"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

//java {
//    toolchain.languageVersion = JavaLanguageVersion.of(21)
//}

tasks {

    shadowJar {
        archiveClassifier.set("all")
    }


    build {
        dependsOn(shadowJar)
    }


    processResources {
        val props = mapOf("version" to project.version)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
