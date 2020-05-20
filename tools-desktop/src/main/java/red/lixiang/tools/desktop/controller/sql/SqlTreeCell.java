package red.lixiang.tools.desktop.controller.sql;

/**
 * @author lixiang
 * @date 2020/4/11
 **/
public class SqlTreeCell {
    public static final int TYPE_CONN =0;
    public static final int TYPE_SCHEME = 1;
    public static final int TYPE_TABLE = 2;
    public static final int TYPE_FIELD = 3;

    private Long id;
    private String showName ;
    private Integer type;
    private String schemeName;
    private String tableName;
    private String fieldName;


    public static SqlTreeCell create(String showName,Integer type){
        SqlTreeCell cell = new SqlTreeCell();
        cell.setShowName(showName).setType(type);
        return cell;
    }


    public String getSchemeName() {
        return schemeName;
    }

    public SqlTreeCell setSchemeName(String schemeName) {
        this.schemeName = schemeName;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public SqlTreeCell setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SqlTreeCell setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SqlTreeCell setId(Long id) {
        this.id = id;
        return this;
    }

    public String getShowName() {
        return showName;
    }

    public SqlTreeCell setShowName(String showName) {
        this.showName = showName;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public SqlTreeCell setType(Integer type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return showName;
    }
}
