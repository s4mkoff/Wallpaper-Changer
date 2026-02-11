package s4.tools.wallpaper_changer.data

class LinkBuilder(
    domain: String
) {

    var params = mutableSetOf<Pair<String, String>>()

    var link = domain

    fun addDestination(
        destination: String
    ) {
        link+="/$destination"
    }

    fun append(
        key: String, value: String
    ) {
        val prefix = if (params.isEmpty()) "/?" else "&"
        params.add(key to value)
        link += "$prefix$key=$value"
    }

    fun build(): String {
        val buildedLink = "https://$link"
        println("Builded api call: $buildedLink")
        return buildedLink
    }

}