import java.io.File
import java.security.MessageDigest

class SecureAutomationScriptParser(val scriptPath: String) {

    private val parserRegex = """(execute|run) "(.+)"""".toRegex()

    fun parseScript(): Map<String, String> {
        val scriptFile = File(scriptPath)
        val scriptContent = scriptFile.readText()
        val scriptHash = calculateHash(scriptContent)

        val commands = mutableMapOf<String, String>()
        parserRegex.findAll(scriptContent).forEach { match ->
            val command = match.groupValues[1]
            val argument = match.groupValues[2]
            commands[command] = argument
        }

        return commands
    }

    private fun calculateHash(content: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(content.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}

fun main() {
    val parser = SecureAutomationScriptParser("path/to/script.txt")
    val commands = parser.parseScript()
    println("Parsed Commands: $commands")
}