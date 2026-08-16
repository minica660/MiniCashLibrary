plugins {
    java
    id("java-library")

    id("com.gradleup.shadow") version "9.6.1" apply false
    id("maven-publish")

//    id("xyz.jpenilla.run-paper") version "3.1.0" apply false
}

allprojects {
    group = "com.github.minica660"
    version = "1.0.5"

}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    configure<JavaPluginExtension>(){
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<JavaCompile>(){
        options.encoding = "UTF-8"
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }


}

tasks.register<Copy>("buildAll") {
    group = "build"
    description = "Paper用とVelocity用のJarを一括ビルドし、Rootのbuild/libs/にまとめてコピーします"

    // 各サブプロジェクトの shadowJar タスクに依存させる
    dependsOn(":paper:shadowJar", ":velocity:shadowJar")

    from(project(":paper").tasks.named("shadowJar"))
    from(project(":velocity").tasks.named("shadowJar"))

    into(layout.buildDirectory.dir("libs"))

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


