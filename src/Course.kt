class Course(name: String, link: String) {
    var courseName: String = name
    var courseLink: String = link
    lateinit var events: ArrayList<String>

    fun setEventsList(eventsList: ArrayList<String>){
        events = eventsList
    }

}