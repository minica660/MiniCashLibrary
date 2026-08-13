plugins {
    java
    id("java-library")
    id("com.gradleup.shadow") version "9.6.1" apply false
//    id("xyz.jpenilla.run-paper") version "3.1.0" apply false
}

allprojects {
    group = "com.example"
    version = "1.0"

}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension>(){
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<JavaCompile>(){
        options.encoding = "UTF-8"
    }

}

// 親プロジェクトで一括ビルド・コピーを行うカスタムタスク
tasks.register("buildAll") {
    group = "build"
    description = "すべてのモジュールをビルドし、出力を build/libs/ に行います"

    dependsOn(":paper:shadowJar", ":velocity:shadowJar")

    doLast {
        val outputDir = file("$rootDir/build/libs")
        outputDir.mkdirs()

        copy {
            from(project(":paper").layout.buildDirectory.dir("libs"))
            from(project(":velocity").layout.buildDirectory.dir("libs"))
            include("*-all.jar")
            into(outputDir)
        }
    }
}

//repositories {
//    mavenCentral()
//    maven("https://repo.papermc.io/repository/maven-public/")
//}
//
//dependencies {
//    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
//}
//
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
