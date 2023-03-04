
group = "org.maurycy.framework"
version = "1.0.0-SNAPSHOT"

tasks.register("buildFull"){
    group="local"
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
}

tasks.register("clean") {
    group="local"
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}