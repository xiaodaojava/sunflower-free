package red.lixiang.tools.desktop.controller.sql;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import red.lixiang.tools.common.mybatis.generate.MybatisGenerateTools;
import red.lixiang.tools.jdk.ListTools;
import red.lixiang.tools.jdk.domain.DomainDesc;
import red.lixiang.tools.jdk.sql.SqlConfig;
import red.lixiang.tools.jdk.sql.SqlTools;
import red.lixiang.tools.jdk.sql.model.SqlField;
import red.lixiang.tools.jdk.sql.model.SqlScheme;
import red.lixiang.tools.jdk.sql.model.SqlTable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static red.lixiang.tools.desktop.controller.sql.SqlTreeCell.*;

/**
 * @author lixiang
 * @date 2020/4/7
 **/
public class SqlConController {

    public TreeView<SqlTreeCell> sqlTree;

    public TableView resultTable;

    private Stage stage;

    private SqlConfig sqlConfig;

    private List<SqlScheme> schemeList;

    private SqlTable currentTable;


    public void initTree(){
        // 绑定双击事件
        sqlTree.setOnMouseClicked(event -> {
            if(event.getClickCount()==2){
                TreeItem<SqlTreeCell> item = sqlTree.getSelectionModel().getSelectedItem();
                SqlTreeCell cellValue = item.getValue();
                switch (cellValue.getType()){
                    case TYPE_SCHEME -> {
                        List<SqlTable> tableList = SqlTools.tableInfo(cellValue.getShowName(), sqlConfig);
                        for (SqlScheme sqlScheme : schemeList) {
                            if(sqlScheme.getName().equals(cellValue.getShowName())){
                                sqlScheme.setTableList(tableList);
                                break;
                            }
                        }
                        List<TreeItem<SqlTreeCell>> cellList = tableList.stream()
                                .map(x -> {
                                    SqlTreeCell cell = create(x.getTableName(), TYPE_TABLE);
                                    cell.setSchemeName(cellValue.getShowName());
                                    TreeItem<SqlTreeCell> treeItem = new TreeItem<>(cell);

                                    return treeItem;
                                }).collect(Collectors.toList());
                        item.getChildren().addAll(cellList);
                    }
                    case TYPE_TABLE->{
                        List<SqlField> sqlFields = SqlTools.columnInfo(cellValue.getSchemeName(),cellValue.getShowName(), sqlConfig);
                        List<TreeItem<SqlTreeCell>> itemList = sqlFields.stream().map(x -> {
                            SqlTreeCell cell = create(x.getName(), TYPE_FIELD);
                            cell.setTableName(cellValue.getShowName());
                            return new TreeItem<>(cell);
                        }).collect(Collectors.toList());
                        item.getChildren().addAll(itemList);
                    }
                    default -> System.out.println();
                }
            }
        });

        // 绑定右键菜单
        MenuItem tableMenuItem = new MenuItem("获取前100行");
        tableMenuItem.setOnAction(e -> {
            TreeItem<SqlTreeCell> item = sqlTree.getSelectionModel().getSelectedItem();
            SqlTreeCell cell = item.getValue();
            String selectSql  = String.format("select * from %s limit 100 offset 0 ",cell.getShowName());
            sqlConfig.setTargetDb(cell.getSchemeName());
            // 设置当前查询的表
            currentTable = schemeList.stream()
                    .filter(x->x.getName().equals(cell.getSchemeName()))
                    .flatMap(x->x.getTableList().stream())
                    .filter(x->x.getTableName().equals(cell.getShowName()))
                    .findFirst().get();
            List<Map<String, Object>> list = SqlTools.tableDetail(selectSql, sqlConfig);
            //把数据库结果转成tableView
            if(ListTools.isNotBlank(list)){
                Map<String, Object> map = list.get(0);
                String[] keysArray = map.keySet().toArray(new String[0]);
                for (int i = 0; i < keysArray.length; i++) {
                    final  int j =i;
                    TableColumn col = new TableColumn(keysArray[i]);
                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param
                            -> {
                       SimpleStringProperty property;
                       if(param.getValue().get(j)!=null){
                           property = new SimpleStringProperty(param.getValue().get(j).toString());
                       }else {
                           property = new SimpleStringProperty(null);
                       }
                       return property;
                    });
                    resultTable.getColumns().add(col);
                }
                for (String s : map.keySet()) {
                }
            }
            ObservableList data = FXCollections.observableArrayList();
            for (Map<String, Object> map : list) {
                ObservableList<String> row = FXCollections.observableArrayList();
                map.forEach((key,value)->{
                    if(value!=null){
                        row.add(value.toString());
                    }else {
                        row.add(null);
                    }
                });
                data.add(row);
            }
            resultTable.setItems(data);
        });
        sqlTree.setContextMenu(new ContextMenu(tableMenuItem));

        TreeItem<SqlTreeCell> rootItem = new TreeItem<>(SqlTreeCell.create(sqlConfig.getTag(), TYPE_CONN));
        rootItem.setExpanded(true);
        for (SqlScheme sqlScheme : schemeList) {
            //第一步,只展示数据库
            TreeItem<SqlTreeCell> schemeItem = new TreeItem<>(SqlTreeCell.create(sqlScheme.getName(),TYPE_SCHEME));
            rootItem.getChildren().add(schemeItem);
        }
        sqlTree.setRoot(rootItem);
    }




    public void initConn(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
        //获取数据库,表等信息
        List<SqlScheme> sqlSchemes = SqlTools.schemeInfo(sqlConfig);

        schemeList = sqlSchemes;
        initTree();
    }

    public void setStage(Stage conStage) {
        this.stage = conStage;
    }

    public void close() {
        if(sqlConfig!=null){
            sqlConfig.close();
        }
    }

    public void newQuery(ActionEvent actionEvent) {

    }

    /**
     * 生成Mybatis相关的文件
     * 有:java实体类. Dao层, Manager层, xml 文件, provider文件, mapper文件, controller 文件
     * jdbc文件等
     * @param actionEvent
     */
    public void generateMybatis(ActionEvent actionEvent) {
        List<SqlField> sqlFields = SqlTools.columnInfo(currentTable.getSchemeName(), currentTable.getTableName(), sqlConfig);
        currentTable.setFieldList(sqlFields);
        DomainDesc domainDesc = MybatisGenerateTools.getDomainDescFromTable(currentTable, "");
        //弹出一个文件选择框,要保存到哪里
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择要下载文件的位置");
        File file = chooser.showDialog(stage);
        var filePath = file.getAbsolutePath();
        domainDesc.toFile(filePath);
    }
}
