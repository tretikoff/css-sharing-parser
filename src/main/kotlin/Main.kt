import smile.clustering.hclust
import smile.math.distance.Distance
import java.io.File
import java.lang.Math.sqrt
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.math.pow


val filenames = listOf(
    "chat.txt",
    "commits_check.txt",
    "commits_page.txt",
    "main_page.txt",
//    "main_page_then_chats.txt",
)

class Main

fun main() {
    val map = mutableMapOf<String, MutableMap<String, Int>>()
    val namesSet = HashSet<String>()
    for (filename in filenames) {
        val cnt = File(Main::class.java.getResource(filename).path).readText();
        val blocks = cnt.removePrefix("{").removeSuffix("}").split("}{")
        for (block in blocks) {
            val rules =
                block.split(", ").filter { it.split("=").size > 1 && !it.startsWith("--") }.map { it.split("=")[0] }
            for (ruleIn in rules) {
                namesSet.add(ruleIn)
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
                    printed = true
                }
            }
        }

    }

    val distance = Distance<String>(function = { name1, name2 ->
        if (map.containsKey(name1) && map[name1]!!.containsKey(name2)) {
            val count = (map[name1]!![name2]!! + 1).toDouble()
            val x = 1.0 / count.pow(1.0 / 3.0)
            x
        } else {
            1.0
        }
    })

    val names = namesSet.toList()
    val clusters = hclust(names.toTypedArray(), distance, "single")
    for (i in 2 until 100) {
        val mp = mutableMapOf<Int, String>()
        val partition = clusters.partition(i)
        for (j in partition.indices) {
            mp[partition[j]] = mp.getOrDefault(partition[j], "") + ", " + names[j]
        }
        val source: Path = Paths.get(Main::class.java.getResource(".").path.replace("/", "\\").removePrefix("\\"))
        val dir = source.toAbsolutePath().toString() + File.separator + "clustering" + File.separator;
        Files.createDirectories(Paths.get(dir))
        val filePath = Paths.get("$dir$i.txt")
        Files.deleteIfExists(filePath)
        Files.createFile(filePath)
        val str = mp.entries.joinToString("\n") { it.value.removePrefix(", ") }
        Files.writeString(filePath, str)
    }
}