import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

group = "tech.laihz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}



dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web"){
        exclude(group="org.springframework.boot" , module = "spring-boot-starter-tomcat")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.projectlombok:lombok")
    implementation("org.junit.jupiter:junit-jupiter-api")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.1")
    implementation("mysql:mysql-connector-java")
    implementation("com.alibaba:fastjson:1.2.62")
    implementation("org.springframework.boot:spring-boot-starter-undertow")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.squareup.okhttp3:okhttp:4.3.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
