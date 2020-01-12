package qu_Item.Search.qu_untity;

import java.util.Map;

public class searchParams {

    private String key;//参数;

    private Integer page;//页数;

    private Map<String,String> filter; //规格参数过滤的条件;

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    //定义每页常量数据; 防止页面乱传数据;
    private static final int ROWS = 10;

    //默认页的大小
    private static  final int PAGES = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        //默认返回默认值;
        if (page == null){
            return PAGES;
        }
        //这样保证返回值不小于1;
        return Math.max(PAGES,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return ROWS;
    }
}
