class Course(name: String, link: String) {
    var courseName: String = name
    var courseLink: String = link
    lateinit var event: String
    lateinit var date: String
    lateinit var time: String

    fun setEventString(eventTime: String) {
        event = eventTime
    }

    fun generateDate() {
        //if event string is empty (means we don't have data of date and time):
        if (event.length == 0) {
            date = ""
            time = ""
        } else if (event.substring(0, 6) == "Moodle") {
            if (event.length == 6) { //if it contains only "Moodle" --> it doesn't conclude info about time
                date = ""
                time = ""
            } else { //if it contains "Moodle" and also info about date and time
                date = event.substring(10, 15)
                date += ".2022"
                date = date.replace(".", "-")
                time = event.substring(17, 27)
            }
        } else if (event.substring(0, 2) == "Gr") { //if it contains multiple groups, ignore it
            date = ""
            time = ""
        } else {
            date = event.substring(3, 8)
            date += ".2022"
            date = date.replace(".", "-")
            time = event.substring(10, 20)
        }
    }
}

