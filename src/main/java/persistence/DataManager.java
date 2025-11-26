package persistence;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import controllers.ElectionSystemController;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DataManager {

    private static final String FILE = "data/system.xml";

    private static XStream xstream = new XStream(new DomDriver());

    static {
        // Allow your model classes
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[]{
                "models.**",
                "datastructures.**",
                "controllers.**"
        });
    }

    // SAVE ================================================================
    public static void save(ElectionSystemController system) throws Exception {
        java.io.File folder = new java.io.File("data");
        if (!folder.exists()) folder.mkdirs();

        try (FileOutputStream out = new FileOutputStream(FILE)) {
            xstream.toXML(system, out);
        }
    }

    // LOAD ================================================================
    public static ElectionSystemController load() throws Exception {
        try (FileInputStream in = new FileInputStream(FILE)) {
            return (ElectionSystemController) xstream.fromXML(in);
        }
    }

    public static boolean hasDataFile() {
        return new java.io.File(FILE).exists();
    }
}
