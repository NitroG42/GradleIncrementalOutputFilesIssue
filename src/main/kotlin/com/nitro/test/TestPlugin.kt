package com.nitro.test

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            tasks.register("gentask", TestTask::class.java) {
                group = "Tasks"
                description = "gen task"
                source = project.fileTree("resgen")
            }
        }
    }

}