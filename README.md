# Gradle: Weird behavior with @Outputfiles/InputChanges

[Issue](https://github.com/gradle/gradle/issues/26039)

## Reproduce

Run the test `testTasksContent` in PluginTester

or `./gradlew tests`

### Context

A task read a txt file as input and generate a file for each line read.
`@InputFiles`
a configurablefilecollection filtering on txt files
`@OutputFiles`
a `ConfigurableFileCollection` (I tried with a `Map<String, File>` too)

### Incremental test

We write one line on the txt file, then add another one.
This test purpose is to check that the first file is not "generated" again by comparing lastModified date ; because
generating those file can take time and power.

## Issue

Right now, after some try and retry, it seems that modifying/adding a file in OutputFiles will trigger the following
message :
> Task :gentask
> Caching disabled for task ':gentask' because:
> Build cache is disabled
> Task ':gentask' is not up-to-date because:
> Output property 'generatedOutputs$2' has been added for task ':gentask'
> The input changes require a full rebuild for incremental task ':gentask'.
> incremental : false
>

which would not be an issue if the current generated outputs weren't removed by Gradle.

# Why ?
