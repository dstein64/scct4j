package datarepo.server;


import java.io.File;
import java.util.logging.Level;

import javax.servlet.jsp.JspFactory;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.jasper.runtime.JspFactoryImpl;

import datarepo.MyLogger;

public class Main extends Thread {
    
    int port = 8080;
    
    public Main(int port){
        this.port = port;
    }
    
    public void run(){
        File docBase = new File("./html");
              
        Tomcat tomcat = new Tomcat();
        
        // Don't do the following. It will pollute your docBase (WebContent) directory
        //tomcat.setBaseDir(docBase.toString());
        
        tomcat.setPort(port);
        tomcat.getConnector().setProperty("maxThreads", "2000");
        tomcat.getConnector().setProperty("acceptCount", "2000");
        tomcat.getConnector().setProperty("compression", "on");
        // Supress server version in Server header (e.g., Server:Apache-Coyote/1.1) - doesn't suppress for error pages though
        // see http://www.techstacks.com/howto/suppress-server-identity-in-tomcat.html
        tomcat.getConnector().setProperty("server", "Apache Tomcat");
        tomcat.getConnector().setProperty("compressableMimeType", "text/html,text/xml,text/plain,application/json,text/javascript");

        String contextPath = "/"; //URL prefix for app
        final Context ctx = tomcat.addContext(contextPath, docBase.getAbsolutePath());
        
        // The following code is taken from Tomcat::initWebappDefaults and slightly modified
        
        // Default servlet 
        Wrapper defaultServlet = Tomcat.addServlet(
                ctx, "default", "org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);

        // JSP servlet (by class name - to avoid loading all deps)
        Wrapper jspServlet = Tomcat.addServlet(
                ctx, "jsp", "org.apache.jasper.servlet.JspServlet");
        JspFactory.setDefaultFactory(new JspFactoryImpl()); // <- wasn't necessary on earlier versions of embedded tomcat
        jspServlet.addInitParameter("fork", "false");
        jspServlet.setLoadOnStartup(3);
        jspServlet.setOverridable(true);

        // Servlet mappings
        ctx.addServletMapping("/", "default");
        ctx.addServletMapping("*.jsp", "jsp");
        ctx.addServletMapping("*.jspx", "jsp");

        // Sessions
        ctx.setSessionTimeout(30);
        
        // MIME mappings
        for (int i = 0; i < DEFAULT_MIME_MAPPINGS.length;) {
            ctx.addMimeMapping(DEFAULT_MIME_MAPPINGS[i++],
                    DEFAULT_MIME_MAPPINGS[i++]);
        }
        
        // Welcome files
        ctx.addWelcomeFile("index.html");
        ctx.addWelcomeFile("index.htm");
        ctx.addWelcomeFile("index.jsp");
        
        // Modifications
        // 0 is the least verbose and 99 is the most verbose
        defaultServlet.addInitParameter("debug", "0");
        // directory listings are seemingly off by default, but doesn't to be explicit
        defaultServlet.addInitParameter("listings", "false");
        jspServlet.addInitParameter("xpoweredBy", "false");
        
        // Custom Servlets
        
        
        // Custom Filters
        
        
        
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            MyLogger.log.log(Level.SEVERE, "LifecycleException", e);
            System.exit(1);
        }
        
        // make sure we started up properly (for example, port already in use would prevent startup)
        // tomcat.getServer... reports STARTED (even when connector reports FAILED)
        if (tomcat.getConnector().getState() == LifecycleState.FAILED) {
            MyLogger.log.log(Level.SEVERE, "Error Starting Server");
        	System.exit(1);
        }
        
        tomcat.getServer().await();
    }
    
    // Default mime mappings is from Tomcat::initWebappDefaults
    private static final String[] DEFAULT_MIME_MAPPINGS = { "abs", "audio/x-mpeg", "ai", "application/postscript",
    														"aif", "audio/x-aiff", "aifc", "audio/x-aiff", "aiff",
    														"audio/x-aiff", "aim", "application/x-aim", "art",
    														"image/x-jg", "asf", "video/x-ms-asf", "asx", "video/x-ms-asf",
    														"au", "audio/basic", "avi", "video/x-msvideo", "avx",
    														"video/x-rad-screenplay", "bcpio", "application/x-bcpio",
    														"bin", "application/octet-stream", "bmp", "image/bmp", "body",
    														"text/html", "cdf", "application/x-cdf", "cer",
    														"application/pkix-cert", "class", "application/java",
    														"cpio", "application/x-cpio", "csh", "application/x-csh",
    														"css", "text/css", "dib", "image/bmp", "doc",
    														"application/msword", "dtd", "application/xml-dtd", "dv",
    														"video/x-dv", "dvi", "application/x-dvi", "eps",
    														"application/postscript", "etx", "text/x-setext", "exe",
    														"application/octet-stream", "gif", "image/gif", "gtar",
    														"application/x-gtar", "gz", "application/x-gzip", "hdf",
    														"application/x-hdf", "hqx", "application/mac-binhex40", "htc",
    														"text/x-component", "htm", "text/html", "html", "text/html",
    														"ief", "image/ief", "jad", "text/vnd.sun.j2me.app-descriptor",
    														"jar", "application/java-archive", "java", "text/x-java-source",
    														"jnlp", "application/x-java-jnlp-file", "jpe", "image/jpeg",
    														"jpeg", "image/jpeg", "jpg", "image/jpeg", "js",
    														"application/javascript", "jsf", "text/plain", "jspf",
    														"text/plain", "kar", "audio/midi", "latex", "application/x-latex",
    														"m3u", "audio/x-mpegurl", "mac", "image/x-macpaint", "man",
    														"text/troff", "mathml", "application/mathml+xml", "me",
    														"text/troff", "mid", "audio/midi", "midi", "audio/midi", "mif",
    														"application/x-mif", "mov", "video/quicktime", "movie",
    														"video/x-sgi-movie", "mp1", "audio/mpeg", "mp2", "audio/mpeg",
    														"mp3", "audio/mpeg", "mp4", "video/mp4", "mpa", "audio/mpeg",
    														"mpe", "video/mpeg", "mpeg", "video/mpeg", "mpega",
    														"audio/x-mpeg", "mpg", "video/mpeg", "mpv2", "video/mpeg2",
    														"nc", "application/x-netcdf", "oda", "application/oda", "odb",
    														"application/vnd.oasis.opendocument.database", "odc",
    														"application/vnd.oasis.opendocument.chart", "odf",
    														"application/vnd.oasis.opendocument.formula", "odg",
    														"application/vnd.oasis.opendocument.graphics", "odi",
    														"application/vnd.oasis.opendocument.image", "odm",
    														"application/vnd.oasis.opendocument.text-master", "odp",
    														"application/vnd.oasis.opendocument.presentation", "ods",
    														"application/vnd.oasis.opendocument.spreadsheet", "odt",
    														"application/vnd.oasis.opendocument.text", "otg",
    														"application/vnd.oasis.opendocument.graphics-template", "oth",
    														"application/vnd.oasis.opendocument.text-web", "otp",
    														"application/vnd.oasis.opendocument.presentation-template", "ots",
    														"application/vnd.oasis.opendocument.spreadsheet-template ", "ott",
    														"application/vnd.oasis.opendocument.text-template", "ogx",
    														"application/ogg", "ogv", "video/ogg", "oga", "audio/ogg",
    														"ogg", "audio/ogg", "spx", "audio/ogg", "flac", "audio/flac",
    														"anx", "application/annodex", "axa", "audio/annodex", "axv",
    														"video/annodex", "xspf", "application/xspf+xml", "pbm",
    														"image/x-portable-bitmap", "pct", "image/pict", "pdf",
    														"application/pdf", "pgm", "image/x-portable-graymap", "pic",
    														"image/pict", "pict", "image/pict", "pls", "audio/x-scpls",
    														"png", "image/png", "pnm", "image/x-portable-anymap", "pnt",
    														"image/x-macpaint", "ppm", "image/x-portable-pixmap", "ppt",
    														"application/vnd.ms-powerpoint", "pps",
    														"application/vnd.ms-powerpoint", "ps",
    														"application/postscript", "psd", "image/vnd.adobe.photoshop",
    														"qt", "video/quicktime", "qti", "image/x-quicktime", "qtif",
    														"image/x-quicktime", "ras", "image/x-cmu-raster", "rdf",
    														"application/rdf+xml", "rgb", "image/x-rgb", "rm",
    														"application/vnd.rn-realmedia", "roff", "text/troff", "rtf",
    														"application/rtf", "rtx", "text/richtext", "sh",
    														"application/x-sh", "shar", "application/x-shar",
    														/*"shtml", "text/x-server-parsed-html", */"sit",
    														"application/x-stuffit", "snd", "audio/basic", "src",
    														"application/x-wais-source", "sv4cpio", "application/x-sv4cpio",
    														"sv4crc", "application/x-sv4crc", "svg", "image/svg+xml",
    														"svgz", "image/svg+xml", "swf", "application/x-shockwave-flash",
    														"t", "text/troff", "tar", "application/x-tar", "tcl",
    														"application/x-tcl", "tex", "application/x-tex", "texi",
    														"application/x-texinfo", "texinfo", "application/x-texinfo",
    														"tif", "image/tiff", "tiff", "image/tiff", "tr", "text/troff",
    														"tsv", "text/tab-separated-values", "txt", "text/plain", "ulw",
    														"audio/basic", "ustar", "application/x-ustar", "vxml",
    														"application/voicexml+xml", "xbm", "image/x-xbitmap", "xht",
    														"application/xhtml+xml", "xhtml", "application/xhtml+xml",
    														"xls", "application/vnd.ms-excel", "xml", "application/xml",
    														"xpm", "image/x-xpixmap", "xsl", "application/xml", "xslt",
    														"application/xslt+xml", "xul", "application/vnd.mozilla.xul+xml",
    														"xwd", "image/x-xwindowdump", "vsd", "application/vnd.visio",
    														"wav", "audio/x-wav", "wbmp", "image/vnd.wap.wbmp", "wml",
    														"text/vnd.wap.wml", "wmlc", "application/vnd.wap.wmlc", "wmls",
    														"text/vnd.wap.wmlsc", "wmlscriptc", "application/vnd.wap.wmlscriptc",
    														"wmv", "video/x-ms-wmv", "wrl", "model/vrml", "wspolicy",
    														"application/wspolicy+xml", "Z", "application/x-compress", "z",
    														"application/x-compress", "zip", "application/zip" };
    
    public static void main(String[] args) throws Exception{
        
        int port = 8765;
        if(args.length!=0)
            port = Integer.parseInt(args[0]);
        
        new Main(port).start();
    }
}

