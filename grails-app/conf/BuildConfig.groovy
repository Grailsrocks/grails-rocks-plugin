grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'


    repositories {
        grailsCentral()
    }

    plugins {
        build(":release:1.0.1", ":svn:1.0.2") {
            export = false
        }

        compile(':resources:1.1.6')
        compile(':zendesk:1.0.4')
    }
}
