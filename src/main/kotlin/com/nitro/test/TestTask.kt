package com.nitro.test

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.work.InputChanges
import javax.inject.Inject

abstract class TestTask @Inject constructor() : DefaultTask() {
    @get:InputFiles
    @get:SkipWhenEmpty
    @get:IgnoreEmptyDirectories
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract var source: ConfigurableFileTree

    @get:OutputFiles
    val generatedOutputs: FileCollection
        get() {
            val lines = source.singleFile.readLines()
            return if (lines.count() >= 2) {
                project.files("test.txt", "test2.txt")
            } else {
                project.files("test.txt")
            }
        }

    @TaskAction
    fun write(inputs: InputChanges) {
        logger.error("incremental : ${inputs.isIncremental}")
        if (!inputs.isIncremental) {
            project.file("test.txt").writeText("coucou")
        }
    }
}