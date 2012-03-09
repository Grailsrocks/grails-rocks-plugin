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
        runtime ":rocks:1.0.1"
        runtime ":zendesk:1.0.4"
    }
}

For details of commercial support options for the Grailsrocks plugins
please see http://grailsrocks.com

To use this plugin browse to http://localhost:8080/<appname>/rocks
"""