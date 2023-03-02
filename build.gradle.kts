
group = "org.maurycy.framework"
version = "1.0.0-SNAPSHOT"

tasks.register("buildFull"){
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
}

tasks.register("clean") {
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}