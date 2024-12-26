package com.yang.fileUpload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface FileMapper {
    void createTable(@Param("tableName")String tableName, @Param("colDefinition") LinkedHashMap<String, String> colDefinition);

    void loadData(@Param("filePath") String absolutePath, @Param("tableName") String tableName);

    void batchInsert(@Param("tableName") String finalTableName, @Param("rowData") List<String[]> batchData);

    void deleteTable(@Param("tableName") String tableName);



    // 流式数据读取
    List<LinkedHashMap<String, Object>> streamSelect(@Param("param") HashMap<String, Object> param); //以stream形式从mysql获取数据

    List<String> getFiledList(@Param("tableName")String tableName);
}
