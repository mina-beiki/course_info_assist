import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList


fun main() {

    val scanner = Scanner(System.`in`)
    println("Welcome to your Course Assistant!")
    var doc: org.jsoup.nodes.Document = Jsoup.parse("https://www.google.com/")
    println("Connecting ...")
    println()


    try {
        doc = Jsoup.connect("https://ufind.univie.ac.at/en/vvz.html").userAgent("Firefox").get()
    } catch (e: Exception) {
        println("Network error occurred! Try again.")
    }

    println("Successfully connected!")
    println()

    //find all study programs:
    val fieldsElements: Elements = doc.select("h2.icon-usse-info")
    val fields = ArrayList<String>()
    for (e in fieldsElements) {
        fields.add(e.text())
    }
    var ctr: Int = 0
    for (str in fields) {
        if(ctr<36)
            println("$ctr - $str")
        else if(ctr==45 || ctr==6){
            println("$ctr - $str")
        }
        else {
            val trimmedStr: String = str.substring(5, str.length)
            println("$ctr - $trimmedStr")
        }

        ctr++
    }

    //choose field:
    println()
    println("Choose your field: (Enter index number)")
    val fieldNumber:Int = scanner.nextInt()



    print(doc.body())
}