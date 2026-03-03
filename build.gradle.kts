plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.fml)
    alias(libs.plugins.quick)
    id("maven-publish")
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

version = "$mod_version+kotlin.${libs.versions.kotlin.get()}"

base {
    archivesName = mod_id
}

repositories {
    maven("https://jitpack.io")
}

loom {
    mergedMinecraftJar()
    fml = File("loader/loader-3.4.2.jar")

    mods {
        create(mod_name) {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    minecraft("Minecraft:IsTooEasy:1.6.4-MITE")
    mappings(loom.fmlMCPMappings())
    implementation(files(loom.fml.toPath()))

    pkg(libs.kotlin.stdlib)
    pkg(libs.kotlin.reflect)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fml.mod.json") {
        expand("version" to project.version)
    }
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
