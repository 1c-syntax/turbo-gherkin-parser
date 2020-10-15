import java.util.*

plugins {
    idea
    java
    antlr
    jacoco
    maven
    `maven-publish`
    id("com.github.gradle-git-version-calculator") version "1.1.0"
    id("com.github.hierynomus.license") version "0.15.0"
}

group = "com.github.1c-syntax"
version = gitVersionCalculator.calculateVersion("v")

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val junitVersion = "5.7.0"
val antlrVersion = "4.7.4"
val antlrGroupId = "com.tunnelvisionlabs"
val antlrArtifactId = "antlr4"

dependencies {
    // runtime
    compileOnly(antlrGroupId, antlrArtifactId, antlrVersion)
    antlr(antlrGroupId, antlrArtifactId, antlrVersion)

    implementation("com.github.1c-syntax", "bsl-parser", "8e29bfd87c2a573cba03629f90592e69bab03596") {
        exclude("com.tunnelvisionlabs", "antlr4-annotations")
        exclude("com.ibm.icu", "*")
        exclude("org.antlr", "ST4")
        exclude("org.abego.treelayout", "org.abego.treelayout.core")
        exclude("org.antlr", "antlr-runtime")
        exclude("org.glassfish", "javax.json")
    }

    // common
    compileOnly("commons-io", "commons-io", "2.6")

    // tests
    testImplementation("org.junit.jupiter", "junit-jupiter-api", junitVersion)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
    testImplementation("org.assertj", "assertj-core", "3.17.2")
}

sourceSets {
    main {
        java.srcDirs("src/main/java", "src/main/gen")
        resources.srcDirs("src/main/resources")
    }
    test {
        java.srcDirs("src/test/java")
        resources.srcDirs("src/test/resources")
    }
}

idea {
    module {
        // Marks the already(!) added srcDir as "generated"
        generatedSourceDirs = generatedSourceDirs + file("src/main/gen")
    }
}

tasks.generateGrammarSource {
    arguments = listOf(
            "-visitor",
            "-package",
            "com.github._1c_syntax.turbo.gherkin.parser",
            "-encoding",
            "utf8"
    )
    outputDirectory = file("src/main/gen/com/github/_1c_syntax/turbo/gherkin/parser")
}

tasks.generateGrammarSource {
    doLast {
        tasks.licenseFormatMain.get().actions[0].execute(tasks.licenseFormatMain.get())
    }
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }

    reports {
        html.isEnabled = true
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        xml.destination = File("$buildDir/reports/jacoco/test/jacoco.xml")
    }
}

tasks.clean {
    doFirst {
        delete("src/main/gen", "out")
    }
}

license {
    header = rootProject.file("license/HEADER.txt")

    ext["year"] = "2020-" + Calendar.getInstance().get(Calendar.YEAR)
    ext["name"] = "Valery Maximov <maximovvalery@gmail.com>, 1c-syntax team <www.github.com/1c-syntax>, BIA Technologies team <www.bia-tech.ru>"
    ext["project"] = "Turbo Gherkin Parser"
    exclude("**/*.tokens")
    exclude("**/*.interp")
    exclude("**/*.g4")
    exclude("**/*.feature")
    strictCheck = true
    mapping("java", "SLASHSTAR_STYLE")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["jar"])
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")

                configurations.implementation.get().dependencies.forEach { dependency ->
                    if (dependency !is SelfResolvingDependency) {
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dependency.group)
                        dependencyNode.appendNode("artifactId", dependency.name)
                        dependencyNode.appendNode("version", dependency.version)
                        dependencyNode.appendNode("scope", "runtime")
                    }
                }
            }
        }
    }
}