import grails.util.Environment

class RocksPluginUrlMappings {

	static mappings = {
        if (Environment.current == Environment.DEVELOPMENT) {
		    "/rocks/$action?/$id?"(controller:"rocks")
	    }
	}
}
