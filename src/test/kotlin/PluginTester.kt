import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PluginTester {
    private val runner by lazy {
        GradleRunner.create()
            .withGradleVersion("8.2.1")
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .forwardOutput()
            .withDebug(true)
    }

    @Rule
    @JvmField
    val testProjectDir = TemporaryFolder()

    @BeforeAll
    fun setup() {
        testProjectDir.create()
    }

    private fun givenBuildScript() =
        File(testProjectDir.root, "build.gradle").writeText(
            // language=gradle
            """
            plugins {
                id "com.nitro.test"
            }
        """
        )

    private fun build(vararg arguments: String): BuildResult = runner.withArguments(*arguments).build()

    @Test
    fun testTasksContent() {
        givenBuildScript()
        // We make a resgen folder containing a single input.txt file with "test" wrote in it
        val inputFile = testProjectDir.newFolder("resgen").apply {
            mkdir()
        }.let {
            File(it, "input.txt").apply {
                writeText("test")
            }
        }

        var result = build("gentask", "--info")
        assertEquals(TaskOutcome.SUCCESS, result.task(":gentask")?.outcome)

        // We expect this output to exist
        val outputFile = File(testProjectDir.root, "test.txt")
        assertTrue { outputFile.exists() }

        // We store the output current last modified time
        val changed = outputFile.lastModified()

        Thread.sleep(1000)

        // We modify the input to add a new file to generate
        inputFile.appendText("\ntest2")
        result = build("gentask", "--info")

        // We relaunch the task
        assertEquals(TaskOutcome.SUCCESS, result.task(":gentask")?.outcome)

        // The first output file should not have been touched and therefore the lastModified time should be the same
        val newChanged = outputFile.lastModified()
        assertEquals(changed, newChanged)
    }
}