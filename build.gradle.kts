import org.gradle.kotlin.dsl.support.serviceOf
import java.io.ByteArrayOutputStream
import java.util.Properties

plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.sonarqube")
    id("jacoco")
}

group = "dev.mbo"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    val nettyResolverDnsNativeMacos: String by System.getProperties()
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(libs.bom))
    implementation(platform(libs.library.bom))
    implementation(platform(libs.spring.ai.bom))

    implementation("dev.mbo:spring-kotlin-web")
    implementation("dev.mbo:spring-kotlin-jpa")
    implementation("dev.mbo:kotlin-logging")
    implementation("dev.mbo:kotlin-encryption:1.3.2") // TODO remove version after next library.bom release
    implementation("org.passay:passay:1.6.6") // TODO remove version after next bom release

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-core")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("io.netty:netty-resolver-dns-native-macos:$nettyResolverDnsNativeMacos:osx-aarch_64")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    val javaVersion: String by System.getProperties()

    withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion))
        }
    }

    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<Wrapper> {
        val gradleReleaseVersion: String by System.getProperties()
        gradleVersion = gradleReleaseVersion
        distributionType = Wrapper.DistributionType.BIN
    }

    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
        exclude(
            "application-secure.yml",
            "application-local.yml",
            "application-prod.yml",
        )
    }

    withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(false)
        }
    }

    val createGitProperties by registering {
        group = "build"
        val outputDir = layout.buildDirectory.dir("generated/resources/git").get().asFile
        val outputFile = outputDir.resolve("git.properties")

        outputs.file(outputFile)

        doLast {
            outputDir.mkdirs()
            val props = Properties()
            props.setProperty("commitFull", getCommitHash(project))
            outputFile.writer().use { writer ->
                props.store(writer, null)
            }
        }
    }

    named<ProcessResources>("processResources") {
        dependsOn(createGitProperties)
        from(layout.buildDirectory.dir("generated/resources/git")) {
            into("") // root of classpath
        }
    }
}

sonarqube {
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.projectKey", "t60f")
        property("sonar.projectName", "OR::t60f")
        property("sonar.sources", "src/main/kotlin,src/main/resources")
        property("sonar.exclusions", "**/src/gen/**/*")
    }
}

jacoco {
    val jacocoToolVersion: String by System.getProperties()
    toolVersion = jacocoToolVersion
}

fun getCommitHash(project: Project): String {
    return System.getenv("CI_COMMIT_TAG") ?: try {
        val stdout = ByteArrayOutputStream()
        val execOps: ExecOperations = project.serviceOf()
        execOps.exec {
            workingDir = project.rootDir
            commandLine("git", "rev-parse", "HEAD")
            standardOutput = stdout
            errorOutput = ByteArrayOutputStream()
            isIgnoreExitValue = true
        }
        stdout.toString().trim().takeIf { it.isNotBlank() } ?: "Unknown"
    } catch (_: Exception) {
        "Unknown"
    }
}