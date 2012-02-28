class RocksGrailsPlugin {
    def version = "1.0"
    def grailsVersion = "1.3 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Rocks Plugin"
    def author = "Marc Palmer"
    def authorEmail = "marc@grailsrocks.com"
    def description = 'Provides a UI and tools for working with plugins and submitting commercial support tickets'
    def documentation = "http://grails.org/plugin/rocks"

    def license = "APACHE 2"
    def organization = [ name: "Grailsrocks", url: "http://grailsrocks.com/" ]
    def issueManagement = [ system: "github", url: "http://github.com/Grailsrocks/grails-rocks-plugin/issues" ]
    def scm = [ url: "http://github.com/Grailsrocks/grails-rocks-plugin/" ]
}
