plugins {
    java
    jacoco
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "5.1.0.4882"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "eshop"

val seleniumJavaVersion = "4.41.0"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.9.3"
val junitJupiterVersion = "5.10.2"
val junitPlatformVersion = "1.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web") // Changed from webmvc to standard web starter

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
    testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
    testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
}

tasks.register<Test>(name = "unitTest") {
    description = "Runs unit tests."
    group = "verification"

    filter {
        excludeTestsMatching("*FunctionalTest")
    }
}

tasks.register<Test>(name = "functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    filter {
        includeTestsMatching("*FunctionalTest")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
tasks.test {
    filter {
        excludeTestsMatching("*FunctionalTest")
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

sonarqube {
    properties {
        val sonarProjectKey = System.getenv("SONAR_PROJECT_KEY")?.takeIf { it.isNotBlank() } ?: "eshop"
        property("sonar.projectKey", sonarProjectKey)
        property("sonar.projectName", "eshop")
        System.getenv("SONAR_ORGANIZATION")?.takeIf { it.isNotBlank() }?.let {
            property("sonar.organization", it)
        }
        System.getenv("SONAR_HOST_URL")?.takeIf { it.isNotBlank() }?.let {
            property("sonar.host.url", it)
        }
        System.getenv("SONAR_TOKEN")?.takeIf { it.isNotBlank() }?.let {
            property("sonar.login", it)
            property("sonar.token", it)
        }
    }
}

tasks.named("sonarqube") {
    dependsOn("test", "functionalTest")
}
