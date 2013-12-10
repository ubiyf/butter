package butter.protocol.replies;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-11-19
 * Time: 下午5:57
 */
public class MultiBulkReply {
    private final List<Object> dataList;

    public MultiBulkReply(@Nullable List<Object> dataList) {
        this.dataList = dataList;
    }

    public List<Object> getDataList() {
        return dataList;
    }
}
