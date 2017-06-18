import com.klarna.hiverunner.HiveShell;
import com.klarna.hiverunner.StandaloneHiveRunner;
import com.klarna.hiverunner.annotations.HiveSQL;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.vai.bigdata.Joiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ilya on 18.06.17.
 */
@RunWith(StandaloneHiveRunner.class)
public class HiveTest {
    private Joiner mkPath = new Joiner(File.separator);
    public String TST_HIVE = mkPath.join("src", "test", "resources", "hive");
    public String TST_OUT = mkPath.join("src", "test", "resources", "output");
    private String SCHEME = "TEST_DB";

    private String pathQueryCreateCity = mkPath.join(TST_HIVE, "create_city.hql");
    private String pathQueryCreateCityInfo = mkPath.join(TST_HIVE, "create_city_info.hql");
    private String pathQueryCreateSubsCity = mkPath.join(TST_HIVE, "create_subs_city.hql");


    @HiveSQL(files = {})
    private HiveShell hiveShell;
    @Before
    public void setUp() throws FileNotFoundException {
        TestUtils.addArg("SCHEME", SCHEME + ".");
        String qCreateCity = TestUtils.loadQuery(pathQueryCreateCity, TestUtils.args);
        String qCreateCityInfo = TestUtils.loadQuery(pathQueryCreateCityInfo, TestUtils.args);
        String qCreateSubsCity = TestUtils.loadQuery(pathQueryCreateSubsCity, TestUtils.args);

        hiveShell.execute(String.format("CREATE DATABASE IF NOT EXISTS %s;", SCHEME));

        hiveShell.execute(qCreateCity);
        hiveShell.execute(qCreateCityInfo);
        hiveShell.execute(qCreateSubsCity);
    }

    private void insertInto(String table, String resource) {
        String delimiter = ";";
        String nullValue = "\\N";

        hiveShell.insertInto(SCHEME, table)
                .withAllColumns()
                .addRowsFromDelimited(
                        new File(resource),
                delimiter,
                nullValue
        ).commit();
    }


    @Test
    public void testSuccessCase() throws FileNotFoundException {
        insertInto("city", mkPath.join(TST_OUT, "city.txt"));
        insertInto("subs_city", mkPath.join(TST_OUT, "subs_city.txt"));

        String pathQueryInsertCityInfo = mkPath.join(TST_HIVE, "city_info.hql");
        String qInsertCityInfo = TestUtils.loadQuery(pathQueryInsertCityInfo, TestUtils.args);

        hiveShell.execute(qInsertCityInfo);

        List<Object[]> result = hiveShell.executeStatement("select * from " + SCHEME + ".city_info order by cnt_subs");

        List<Object[]> expected = Arrays.asList(
                new Object[]{"Санкт-Питербург", 1l},
                new Object[]{"Москва", 1l},
                new Object[]{"Воронеж", 2l}
        );

        Assert.assertArrayEquals(result.toArray(), expected.toArray());


    }
}
