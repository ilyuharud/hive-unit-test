import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by ilya on 18.06.17.
 */
public class TestUtils {

    public static Map<String, String> args = new HashMap<String, String>();

    public static void addArg(String name, String val) {
        args.put(name, val);
    }

    private static String replaceArg(String query, Map<String, String> args) {
        for(String item: args.keySet()){
            if(query.contains("${" + item + "}"))
                query = query.replace("${" + item + "}", args.get(item));
        }
        return query;
    }

    public static String loadQuery(String path, Map<String, String> args) throws FileNotFoundException {
        String query = "";
        Scanner in = new Scanner(new File(path));
        while(in.hasNext()){
            query += in.nextLine() + "\r\n";
        }
        in.close();
        return replaceArg(query, args);
    }
}
