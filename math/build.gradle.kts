plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.allopen") version "1.7.22"
    kotlin("kapt") version "1.7.22"
    id("io.quarkus")
    id("org.sonarqube") version "3.5.0.2730"
    id("org.owasp.dependencycheck") version "8.0.2"
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
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-oidc")

    implementation("io.quarkus:quarkus-kubernetes")
    implementation("io.quarkus:quarkus-kind")

    implementation("io.quarkus:quarkus-infinispan-client")
    compileOnly("org.infinispan.protostream:protostream-processor:4.4.1.Final")
    kapt("org.infinispan.protostream:protostream-processor:4.4.1.Final")

    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("org.jfree:jfreesvg:3.4.3")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "org.maurycy.framework"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
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
