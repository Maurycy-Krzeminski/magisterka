plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.allopen") version "1.7.22"
    kotlin("kapt") version "1.7.22"
    id("io.quarkus")
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.sonarqube") version "3.5.0.2730"
    id("org.owasp.dependencycheck") version "8.0.2"
    jacoco
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-container-image-jib")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-grpc")
    implementation("io.quarkus:quarkus-opentelemetry")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive")

    implementation("io.quarkus:quarkus-infinispan-client")
    compileOnly("org.infinispan.protostream:protostream-processor:4.4.1.Final")
    kapt("org.infinispan.protostream:protostream-processor:4.4.1.Final")

    implementation("io.quarkus:quarkus-logging-gelf")

    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("org.jfree:jfreesvg:3.4.3")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.quarkus:quarkus-jacoco")
}

group = "org.maurycy.framework"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    finalizedBy("jacocoTestReport")
}
allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
    dependsOn("test")
}

sonarqube{
    properties {
        property("sonar.host.url", "http://localhost:9000")
    }
}
tasks.test{
    configure<JacocoTaskExtension> {
        isEnabled = true
        includes = emptyList()
        excludes = emptyList()
        excludeClassLoaders = listOf("*QuarkusClassLoader")
    }
}
tasks.named("sonar").configure {
    dependsOn("jacocoTestReport")
}