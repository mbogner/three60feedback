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

        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
        id("org.openapi.generator") version openapiGeneratorVersion
        id("com.rameshkp.openapi-merger-gradle-plugin") version openapiMergerVersion
        id("org.sonarqube") version sonarVersion
    }
}