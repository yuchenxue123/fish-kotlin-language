plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.fml.loom)
    alias(libs.plugins.quick)
    id("maven-publish")
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

// JitPack cannot recognize tags like 1.1.3+kotlin.2.3.10,
// so here we use sub-version numbers like 2310 instead.
val kv = libs.versions.kotlin.get()
    .split(".")
    .map { it.toInt() }
    // 2.10.10 or 2.1.100 ?
    .zip(listOf(1000, 100, 1))
    .sumOf { (k, v) -> k * v }

version = "$mod_version.$kv"

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
    pkg(libs.kotlinx.coroutines)
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
