plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "ca.mta.comp4721.team4"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// https://mvnrepository.com/artifact/org.json/json
	implementation("org.json:json:20210307")
	// https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox
	implementation("org.apache.pdfbox:pdfbox:2.0.23")
	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation("commons-io:commons-io:2.9.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
