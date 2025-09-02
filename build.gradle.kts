plugins {
    kotlin("jvm")
    id("fml-loom")
}

// Mod Info
val mod_name: String by project
val mod_id: String by project
val mod_version: String by project
val mod_description: String by project
// Loader
val loader_version: String by project
val minecraft_version: String by project
// Dependencies
val kotlin_version: String by project

version = "$mod_version+kotlin.$kotlin_version"

base {
    archivesName = mod_name
}

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
}

loom {
    mergedMinecraftJar()
    fml = File("libs/loader-$loader_version.jar")

    mods {
        create(mod_name) {
            sourceSet(sourceSets.main.get())
        }
    }
}

val packageImplementation = configurations.register("packageImplementation") {
    configurations.implementation.get().extendsFrom(this)
}

fun DependencyHandler.packageImplementation(dependencyNotation: Any) {
    add("packageImplementation", dependencyNotation)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.fmlMCPMappings())
    implementation(files(loom.fml.toPath()))

    packageImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    packageImplementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
}

val properties = mapOf(
    "id" to mod_id,
    "name" to mod_name,
    "version" to mod_version,
    "description" to mod_description
)

tasks.processResources {
    inputs.property("version", mod_version)

    filesMatching("fml.mod.json") {
        expand(properties)
    }
}

tasks.jar {
    from({
        packageImplementation.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    }) {
        exclude("LICENSE.txt")
        exclude("META-INF/maven/**")
        exclude("META-INF/versions/**")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    from(arrayOf("LICENSE.txt", "NOTICE.txt")) {
        into("META-INF/")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmToolchain(17)
    }
}

