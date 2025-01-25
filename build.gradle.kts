plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

group = "dev.mbo"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    val nettyResolverDnsNativeMacos: String by System.getProperties()
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
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

}
