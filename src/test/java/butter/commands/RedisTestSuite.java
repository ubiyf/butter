package butter.commands;

import butter.commands.keys.KeysTest;
import butter.commands.lists.ListsTest;
import butter.commands.sets.SetsTest;
import butter.commands.strings.StringsTest;
import butter.commands.zsets.ZSetsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-12
 * Time: 下午6:23
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({KeysTest.class, StringsTest.class, ListsTest.class, SetsTest.class, ZSetsTest.class})
public class RedisTestSuite {
}
