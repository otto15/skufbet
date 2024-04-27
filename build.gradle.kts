val bootRunAll by tasks.registering {
    description = "Runs all Spring Boot services simultaneously"
    dependsOn(
        subprojects.filter {
            !it.name.startsWith("database")
                    && !it.name.startsWith("common")
                    && !it.name.startsWith("utils")
        }.map { it.tasks.named("bootRun") }
    )
    doLast {
        println("All applications started.")
    }
}
