plugins {
	kotlin("jvm") version("2.1.0")
	`maven-publish`
	id("fabric-loom") version "1.9-SNAPSHOT"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
	archivesName.set(project.property("archives_base_name") as String)
}

class Dependencies {
	val minecraft = project.property("minecraft_version")
	val yarnBuild = project.property("yarn_mappings")
	val fabricLoader = project.property("loader_version")
	val fabricApi = project.property("fabric_version")
	val fabricKotlin = project.property("fabric_kotlin_version")
}

val deps = Dependencies()

dependencies {
	minecraft("com.mojang:minecraft:${deps.minecraft}")
	mappings("net.fabricmc:yarn:${deps.minecraft}+build.${deps.yarnBuild}:v2")
	modImplementation("net.fabricmc:fabric-loader:${deps.fabricLoader}")

	modImplementation(fabricApi.module("fabric-command-api-v2", "${deps.fabricApi}+${deps.minecraft}"))
	modImplementation("net.fabricmc:fabric-language-kotlin:${deps.fabricKotlin}")
}

loom {
	// I'd like to split source sets, but Loom is causing issues with Fabric API using it :(

	mods {
		create("commander") {
			sourceSet(sourceSets["main"])
		}
	}
}

val targetJava = 21

java {
	withSourcesJar()
	targetCompatibility = JavaVersion.toVersion(targetJava)
	sourceCompatibility = JavaVersion.toVersion(targetJava)
}

kotlin {
	explicitApi()
	jvmToolchain(targetJava)
}

tasks.processResources {
	inputs.property("version", project.version)
	inputs.property("minecraft_version", project.property("minecraft_version"))
	filteringCharset = "UTF-8"

	filesMatching("fabric.mod.json") {
		expand(
			"version" to project.version,
			"minecraft_version" to project.property("minecraft_version")
		)
	}
}

publishing {
	repositories {
		maven {
			name = "celestialfault"
			url = uri("https://maven.celestialfault.dev/releases")
			credentials {
				username = project.findProperty("maven.username") as String? ?: System.getenv("MAVEN_NAME")
				password = project.findProperty("maven.secret") as String? ?: System.getenv("MAVEN_SECRET")
			}
		}
	}

	publications {
		create<MavenPublication>("commander") {
			from(components["java"])
		}
	}
}
