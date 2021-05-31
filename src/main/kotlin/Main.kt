import java.io.File

val filenames = listOf(
    "chat.txt",
    "commits_check.txt",
    "commits_page.txt",
    "main_page.txt",
    "main_page_then_chats.txt",
)

class Main

fun main() {
    for (filename in filenames) {
        val content = File(Main::class.java.getResource(filename).path).readText();
        val blocks = content.removePrefix("{").removeSuffix("}").split("}{")
        val map = mutableMapOf<String, MutableMap<String, Int>>()
        for (block in blocks) {
            val rules =
                block.split(", ").filter { it.split("=").size > 1 && !it.startsWith("--") }.map { it.split("=")[0] }
            for (ruleIn in rules) {
                for (ruleOut in rules) {
                    if (ruleIn == ruleOut) break
                    if (map.containsKey(ruleIn)) {
                        if (map[ruleIn]!!.containsKey(ruleOut)) {
                            val old = map[ruleIn]!![ruleOut]!!
                            map[ruleIn]!![ruleOut] = old + 1
                        }
                    } else {
                        map[ruleIn] = mutableMapOf(ruleOut to 1)
                    }
                }
            }
        }
        val threshold = 0
        val list = mutableListOf<Pair<String, Int>>()
        for (entry in map.entries.sortedByDescending { it.value.values.sum() }) {
            var printed = false
            for (x in entry.value.entries.sortedByDescending { it.value }.filter { it.value > threshold }) {
                list.add(Pair("${entry.key} ${x.key}", x.value))
                if (!printed) {
//                    println(entry.key)
                    printed = true
                }
//                println("\t${x.key} ${x.value}")
            }
        }
        for (x in list.sortedByDescending { it.second }) {
            println("${x.first} ${x.second}")
        }
    }
}