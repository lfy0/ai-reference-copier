plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
    kotlin("jvm") version "1.9.22"
}

group = "com.github.aireference"
version = "0.5.2"

repositories { mavenCentral() }

intellij {
    intellijRepository.set("https://www.jetbrains.com/intellij-repository")
    ideaDependencyCachePath.set(layout.buildDirectory.dir("idea-dependency-cache").get().asFile.absolutePath)
    val localIdePath = providers.gradleProperty("localIdePath")
    if (localIdePath.isPresent) localPath.set(localIdePath) else {
        version.set("2023.2.5")
        type.set("IC")
    }
    updateSinceUntilBuild.set(false)
}

kotlin { jvmToolchain(17) }

if (providers.gradleProperty("localIdePath").isPresent) {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-Xskip-metadata-version-check"
    }
}

tasks {
    patchPluginXml { sinceBuild.set("232") }
    publishPlugin {
        token.set(providers.gradleProperty("intellijPlatformPublishingToken").orElse(""))
    }
    test { useJUnitPlatform() }
    buildSearchableOptions { enabled = false }
    named<Zip>("buildPlugin") {
        destinationDirectory.set(layout.buildDirectory)
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}
