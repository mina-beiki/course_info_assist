class Course(name: String, link: String) {
    var courseName: String = name
    var courseLink: String = link
    lateinit var event: String
    lateinit var date: String

    fun setEventString(eventTime: String) {
        event = eventTime
    }

    fun generateDate() {
        if (event.isNotEmpty()) {
            if (event.substring(0, 5) == "Moodle") {
                println("moodle")
                date = event.substring(10, 15)
            } else if (event.substring(0, 2) == "Gr.") {
                println("gr")
                date = ""
            } else {
                println("normal")
                date = event.substring(3, 8)
            }
        } else {
            println("empty")
            date = ""
        }
    }

}