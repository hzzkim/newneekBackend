plugins {
	java
	war
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.newneek"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
	implementation("org.springframework.boot:spring-boot-starter-validation:3.3.3")
	implementation("com.nimbusds:nimbus-jose-jwt:9.31") //jwt token
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation ("org.springframework.security:spring-security-oauth2-client")
    implementation ("org.springframework.security:spring-security-oauth2-jose")
    
    implementation ("com.google.api-client:google-api-client:1.33.0")
	implementation ("com.google.oauth-client:google-oauth-client:1.34.1")
	implementation ("com.google.http-client:google-http-client-gson:1.41.0")
	
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	annotationProcessor("org.projectlombok:lombok")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	
}


tasks.withType<Test> {
	useJUnitPlatform()
}



