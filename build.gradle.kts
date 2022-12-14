import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.vaadin") version "23.1.2"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("org.asciidoctor.jvm.pdf") version "3.3.2"
	id("org.eclipse.jkube.openshift") version "1.8.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"

	kotlin("kapt") version "1.7.10"

}

group = "eu.ecodex"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["vaadinVersion"] = "23.1.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	//kapt("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("com.github.mvysny.karibudsl:karibu-dsl:1.0.1")
	//implementation("eu.vaadinonkotlin:vok-framework-v10-vokdb:0.8.1")
	//source: https://search.maven.org/artifact/eu.vaadinonkotlin/vok-framework-v10-vokdb/0.8.1/jar

	implementation("org.springframework.boot:spring-boot-starter-webflux")


	//implementation ("org.springframework.boot:spring-boot-starter-security")
	//testImplementation ("org.springframework.security:spring-security-test")
//	implementation ("org.springframework.boot:spring-boot-starter-data-r2dbc")
//	runtimeOnly ("io.r2dbc:r2dbc-h2")

}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

asciidoctorj {
	getModules().getPdf().version("1.2.3")
	getModules().getPdf().use()
	getModules().getDiagram().version("1.5.16")
	getModules().getDiagram().use()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
