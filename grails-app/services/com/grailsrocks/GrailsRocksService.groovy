package com.grailsrocks

class GrailsRocksService {

    static transactional = false
    
    UserDetails getUserDetails() {
        def data = load()
        return new UserDetails(email:data.email, password:data.zendeskPassword)
    }
    
    void setUserDetails(UserDetails details) {
        def p = new Properties()
        p.email = details.email
        p.zendeskPassword = details.password
        save(p)
    }

    private File getPropsFile() {
        def home = new File(System.getProperty('user.home'))
        new File(home, 'grailsrocks.properties')
    }
    
    private Properties load() {
        def p = new Properties()
        def pf = propsFile
        if (pf.exists()) {
            p.load(pf.newInputStream())
        }
        return p
    }
    
    private void save(Properties p) {
        propsFile.withOutputStream { s -> 
            p.store(s, "It is lame that Java needs this string")
        }
    }
}

class UserDetails {
    String email
    String password
}
