//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//
System.out.println """
  _____           _ _                    _        
 / ____|         (_) |                  | |       
| |  __ _ __ __ _ _| |___ _ __ ___   ___| | _____ 
| | |_ | '__/ _` | | / __| '__/ _ \\ / __| |/ / __|
| |__| | | | (_| | | \\__ \\ | | (_) | (__|   <\\__ \\
 \\_____|_|  \\__,_|_|_|___/_|  \\___/ \\___|_|\\_\\___/

You've installed the Rocks plugin!

This is a development-time only plugin. To ensure this code is not
included in your deployed applications please add the following code
to your BuildConfig.groovy to conditionally include the plugin and
its dependencies.

...
plugins {
    ...
    
    if (grails.util.Environment.current == grails.util.Environment.DEVELOPMENT) {
        runtime ":rocks:1.0"
        runtime ":zendesk:1.0"
    }
}

For details of commercial support options for the Grailsrocks plugins
please see http://grailsrocks.com

To use this plugin browse to http://localhost:8080/<appname>/rocks
"""