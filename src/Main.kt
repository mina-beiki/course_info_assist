import org.jsoup.Jsoup
import java.util.*


fun main() {

    println("Welcome to your Course Assistant!")
    print("Enter the name of course: ")

    //val scanner = Scanner(System.`in`)
    //val courseName: String = scanner.next()
    //println("Course name = $courseName")

    val doc = Jsoup.connect("https://ufind.univie.ac.at/en/vvz.html").get()
    print(doc.body().allElements.toString())
}