rootProject.name = "t60f"


pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        val kotlinVersion: String by System.getProperties()
        val springBootVersion: String by System.getProperties()
        val openapiGeneratorVersion: String by System.getProperties()
        val openapiMergerVersion: String by System.getProperties()
        val sonarVersion: String by System.getProperties()
        val cyclonedxVersion: String by System.getProperties()
        val sentryPluginVersion: String by System.getProperties()

        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
        id("org.openapi.generator") version openapiGeneratorVersion
        id("com.rameshkp.openapi-merger-gradle-plugin") version openapiMergerVersion
        id("org.sonarqube") version sonarVersion
        id("org.cyclonedx.bom") version cyclonedxVersion
        id("io.sentry.jvm.gradle") version sentryPluginVersion
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val bomVersion: String by System.getProperties()
            library("bom", "dev.mbo", "spring-boot-bom").version(bomVersion)

            val libraryBomVersion: String by System.getProperties()
            library("library-bom", "dev.mbo", "library-bom").version(libraryBomVersion)

            val springAiVersion: String by System.getProperties()
            library("spring-ai-bom", "org.springframework.ai", "spring-ai-bom").version(springAiVersion)
        }
    }
}