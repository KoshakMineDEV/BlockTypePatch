plugins {
    java
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://repo.spongepowered.org/maven/")
  maven {
    name = "luminiadevRepositorySnapshots"
    url = uri("https://repo.luminiadev.com/snapshots")
  }
}

dependencies {
  compileOnly("com.koshakmine:Lumi:1.3.0-SNAPSHOT")

  compileOnly(libs.ignite)
  compileOnly(libs.mixin)
  compileOnly(libs.mixinExtras)

  annotationProcessor(libs.mixinExtras)
}
