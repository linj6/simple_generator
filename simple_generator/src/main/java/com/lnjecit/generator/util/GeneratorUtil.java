package com.lnjecit.generator.util;

import com.lnjecit.generator.helper.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 代码生成工具类2
 */
public final class GeneratorUtil {

    private static final String AUTHOR_NAME = "Linj";//作者名称

    //项目路径
    private static final String PROJECT_PATH = "D:\\code_generate\\simple_generator";
    //java类路径
    private static final String CLASSPATH = "/src/main/java/";
    //java资源文件路径
    private static final String RESOURCE_PATH = "/src/main/resources/";
    //dao模板文件路径
    private static final String DAO_TEMPLATE_PATH = "E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\dao.html";
    private static final String MAPPING_TEMPLATE_PATH = "E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\mapping.html";
    //service模板文件路径
    private static final String SERVICE_TEMPLATE_PATH = "E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\service.html";
    //controller模板文件路径
    private static final String CONTROLLER_TEMPLATE_PATH = "E:\\projects\\learn\\code-generator\\simple_generator\\src\\main\\resources\\templates\\controller.html";

    /**
     * 生成实体
     * @param tableNames 表名集合
     * @param entityName 实体名称
     * @param entityPath 实体所属包名
     */
    public static void generateEntity(List<String> tableNames, String entityName, String entityPath) {
        //与数据库的连接
        List<String> columnNames; // 列名数组
        List<String> columnTypes; // 列名类型数组
        List<String> columnComments;//列名注释集合
        for (String tableName : tableNames) {
            columnNames = DatabaseHelper.getColumnNames(tableName);
            columnTypes = DatabaseHelper.getColumnTypes(tableName);
            columnComments = DatabaseHelper.getColumnComments(tableName);
            generateEntityClass(tableName, entityName, columnNames, columnTypes, columnComments, entityPath);
            columnComments.clear();
            System.out.println("Entity已经生成");
        }
    }

    /**
     * 生成接口
     * @param tableNames 表名
     * @param entityName 实体名称
     * @param mapperName 接口文件名
     * @param mapperPath 文件保存路径
     * @return
     */
    private static void generateMapper(List<String> tableNames, String entityName, String entityPath, String mapperName, String mapperPath) {
        for (String tableName : tableNames) {
            //实体名
            String finalEntityName = getEntityName(tableName, entityName);
            //接口名
            String finalMapperName = getMapperName(tableName, entityName, mapperName);
            //读取mapper（接口）模板类
            String serviceTempStr = FileUtil.readFileByChar(DAO_TEMPLATE_PATH);
            String content = serviceTempStr.replace("${daoPackage}", mapperPath);
            content = content.replace("${entityName}", finalEntityName);
            content = content.replace("${entityPath}", entityPath);
            content = content.replace("${entityNameFirstCharLower}", StringUtil.firstCharToLower(finalEntityName));

            //生成文件
            String fileDirectory = PROJECT_PATH + CLASSPATH + mapperPath.replace(".", "/") + "/" + getEntityName(tableName, entityName) + "Dao.java";
            generateFile(fileDirectory, content);
            System.out.println("Mapper接口已经生成");
        }
    }

    /**
     * 生成映射文件
     * @param tableNames 表名集合
     * @param entityName 实体名
     * @param mapperPath 接口文件保存目录
     * @param mapperName 接口名
     * @param mappingPath 映射文件保存路径
     */
    private static void generateMapping(List<String> tableNames, String entityName, String entityPath, String mapperPath, String mapperName, String mappingPath, String mappingName) {
        for (String tableName : tableNames) {
            StringBuffer buffer = new StringBuffer();
            //实体名
            String finalEntityName = getEntityName(tableName, entityName);
            //映射文件命名空间
            String namespace = mapperPath + "." + getMapperName(tableName, entityName, mapperName);
            //baseColumnList
            String baseColumnList = getBaseColumnList(tableName);
            //resultMap
            String resultMap = getResultMap(tableName);
            //getEntityListSql
            String getEntityListSql = getEntityListSql(tableName, entityName);
            //getEntityByIdSql
            String getEntityByIdSql = getEntityByIdSql(tableName);
            //deleteEntityByIdSql
            String deleteEntityByIdSql = deleteEntityByIdSql(tableName);
            //updateEntityByIdSql
            String updateEntityByIdSql = updateEntityByIdSql(tableName);
            //addEntitySql
            String addEntitySql = addEntitySql(tableName, baseColumnList);
            //读取Mapping映射文件模板
            String mappingTempStr = FileUtil.readFileByChar(MAPPING_TEMPLATE_PATH);
            String content = mappingTempStr.replace("${namespace}", namespace);
            content = content.replace("${entityPath}", entityPath);
            content = content.replace("${entityName}", finalEntityName);
            content = content.replace("${entityNameFirstCharLower}", StringUtil.firstCharToLower(finalEntityName));
            content = content.replace("${resultMap}", resultMap);
            content = content.replace("${baseColumnList}", baseColumnList);
            content = content.replace("${getEntityListSql}", getEntityListSql);
            content = content.replace("${getEntityByIdSql}", getEntityByIdSql);
            content = content.replace("${deleteEntityByIdSql}", deleteEntityByIdSql);
            content = content.replace("${updateEntityByIdSql}", updateEntityByIdSql);
            content = content.replace("${addEntitySql}", addEntitySql);

            //生成文件
            String fileDirectory = PROJECT_PATH + RESOURCE_PATH + mappingPath.replace(".", "/") + "/" + getMappingName(tableName, entityName, mappingName) + ".xml";
            generateFile(fileDirectory, content);

            System.out.println("Mapping映射文件已经生成");
        }
    }

    /**
     * 生成Service类
     * @param tableNames 表名集合
     * @param entityName 实体名
     * @param mapperName 接口名
     * @param mapperPath 接口文件保存路径
     */
    private static void generateService(List<String> tableNames, String entityName, String entityPath, String mapperName, String mapperPath, String servicePath) {
        for (String tableName : tableNames) {
            //实体名
            String finalEntityName = getEntityName(tableName, entityName);
            //接口名
            String finalMapperName = getMapperName(tableName, entityName, mapperName);
            //读取Service模板类
            String serviceTempStr = FileUtil.readFileByChar(SERVICE_TEMPLATE_PATH);
            String content = serviceTempStr.replace("${servicePackage}", servicePath);
            content = content.replace("${mapperPath}", mapperPath);
            content = content.replace("${mapperName}", finalMapperName);
            content = content.replace("${entityName}", finalEntityName);
            content = content.replace("${entityPath}", entityPath);
            content = content.replace("${mapperNameFirstCharLower}", StringUtil.firstCharToLower(finalMapperName));
            content = content.replace("${entityNameFirstCharLower}", StringUtil.firstCharToLower(finalEntityName));

            //生成文件
            String fileDirectory = PROJECT_PATH + CLASSPATH + servicePath.replace(".", "/") + "/" + getEntityName(tableName, entityName) + "Service.java";
            generateFile(fileDirectory, content);
            System.out.println("Service已经生成");
        }
    }

    private static void generateController(List<String> tableNames, String entityName, String mapperName, String mapperPath, String servicePath, String controllerPath) {
        for (String tableName : tableNames) {
            //实体名
            String finalEntityName = getEntityName(tableName, entityName);
            //接口名
            String finalMapperName = getMapperName(tableName, entityName, mapperName);
            //读取Controller模板类
            String serviceTempStr = FileUtil.readFileByChar(CONTROLLER_TEMPLATE_PATH);
            String content = serviceTempStr.replace("${controllerPackage}", controllerPath);
            content = content.replace("${servicePackage}", servicePath);
            content = content.replace("${entityName}", finalEntityName);
            content = content.replace("${entityNameFirstCharLower}", StringUtil.firstCharToLower(finalEntityName));

            //生成文件
            String fileDirectory = PROJECT_PATH + CLASSPATH + controllerPath.replace(".", "/") + "/" + getEntityName(tableName, entityName) + "Controller.java";
            generateFile(fileDirectory, content);
            System.out.println("Controller已经生成");
        }
    }

    private static String getBaseColumnList(String tableName) {
        StringBuffer buffer = new StringBuffer();
        List<String> colunmNames = DatabaseHelper.getColumnNames(tableName);
        for(String columnName : colunmNames) {
            buffer.append(columnName + ",");
        }
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String getResultMap(String tableName) {
        String resultColumn = "\t\t<result column=\"";
        StringBuffer buffer = new StringBuffer();
        List<String> colunmNames = DatabaseHelper.getColumnNames(tableName);
        for(String columnName : colunmNames) {
            if (!"id".equals(columnName)) {
                buffer.append(resultColumn + columnName + "\" property=\"" + StringUtil.transferToCamel(columnName, false) + "\"/>\r\n");
            }
        }
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String getEntityListSql(String tableName, String entityName) {
        StringBuffer buffer = new StringBuffer();
        //getEntityList
        buffer.append("SELECT\r\n");
        buffer.append("\t\t<include refid=\"Base_Column_List\"/>\r\n");
        buffer.append("\t\tFROM " + tableName + "\r\n");
        buffer.append(getWhereValues(tableName));
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String getEntityByIdSql(String tableName) {
        StringBuffer buffer = new StringBuffer();
        //getEntityByIdSql
        buffer.append("SELECT\r\n");
        buffer.append("\t\t<include refid=\"Base_Column_List\"/>\r\n");
        buffer.append("\t\tFROM " + tableName + " WHERE id = #{id}\r\n");
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String deleteEntityByIdSql(String tableName) {
        StringBuffer buffer = new StringBuffer();
        //deleteEntityById
        buffer.append("UPDATE " + tableName + " set del = 0\r\n");
        buffer.append("\t\tWHERE id = #{id}\r\n");
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    /**
     * 通过id修改
     * @param tableName 表名
     * @return
     */
    private static String updateEntityByIdSql(String tableName) {
        List<String> columnNames = DatabaseHelper.getColumnNames(tableName);
        StringBuffer buffer = new StringBuffer();
        //updateEntityById <update id="updateRoleCompanyById" parameterType="java.util.HashMap">
        buffer.append("UPDATE " + tableName + "\r\n");
        buffer.append("\t\t<set>\r\n");
        for (String columnName : columnNames) {
            buffer.append("\t\t\t" + columnName + "=" + StringUtil.transferToCamel(columnName, false) + ",\r\n");
        }
        buffer.append("\t\t</set>\r\n");
        buffer.append("\t\tWHERE id = #{id}\r\n");
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    /**
     * 新增实体sql语句
     * @param tableName 表名
     * @param baseColumnList
     * @return
     */
    private static String addEntitySql(String tableName, String baseColumnList) {
        StringBuffer buffer = new StringBuffer();
        //addEntity
        buffer.append("INSERT INTO " + tableName + "(" + baseColumnList + ")\r\n");
        buffer.append("\t\tVALUES (" + getInsertValues(tableName) + ")\r\n");
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String getInsertValues(String tableName) {
        StringBuffer buffer = new StringBuffer();
        List<String> colunmNames = DatabaseHelper.getColumnNames(tableName);
        for (String columnName : colunmNames) {
            buffer.append("#{" + columnName + "},");
        }
        return buffer.toString().substring(0, buffer.length() - 1);
    }

    private static String getWhereValues(String tableName) {
        StringBuffer buffer = new StringBuffer();
        List<String> columnNames = DatabaseHelper.getColumnNames(tableName);
        buffer.append("\t\t<where>\r\n");
        buffer.append("\t\t\t1=1\r\n");
        for (String columnName : columnNames) {
            buffer.append("\t\t\t<if test=\"" + columnName +  " != null\">\r\n");
            buffer.append("\t\t\t\tAND "+ columnName + " = #{" + columnName + "}\r\n");
            buffer.append("\t\t\t</if>\r\n");
        }
        buffer.append("\t\t</where>\r\n");
        return buffer.toString();
    }

    private static String generateMapperMethod(String tableName, String entityName, String mapperName) {
        StringBuffer buffer = new StringBuffer();
        String finalEntityname = getEntityName(tableName, entityName);
        buffer.append("public interface " + getMapperName(tableName, entityName, mapperName) + " {\r\n\r\n");
        buffer.append("\tHashMap get" + finalEntityname + "ById(Map<String, Object> params" + ");\r\n\r\n");
        buffer.append("\tList<HashMap> get" + finalEntityname + "List(Map<String, Object> params" + ");\r\n\r\n");
        buffer.append("\tvoid add" + finalEntityname + "(Map<String, Object> params" + ");\r\n\r\n");
        buffer.append("\tvoid update" + finalEntityname + "ById(@Param(\"params\")Map<String, Object> params" + ");\r\n\r\n");
        buffer.append("\tvoid delete" + finalEntityname + "ById(Map<String, Object> params" + ");\r\n\r\n");
        buffer.append("}\r\n");
        return buffer.toString();
    }


    /**
     * 生成实体类.java文件
     *
     * @param columnNames    列名数组
     * @param columnTypes    列名类型数组
     * @param columnComments 列名注释数组
     * @param entityPath     实体所属包名
     */
    private static void generateEntityClass(String tableName, String entityName, List<String> columnNames, List<String> columnTypes, List<String> columnComments, String entityPath) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("package " + entityPath + ";\r\n");
        buffer.append("\r\n");
        // 判断是否导入工具包
        if (hasDateType(columnTypes)) {
            buffer.append("import java.util.Date;\r\n");
        }
        //实现序列化接口
        buffer.append("import java.io.Serializable;\r\n\r\n");
        // 类的注释部分
        buffer.append(generateClassComment());
        // 实体部分
        buffer.append("public class " + getEntityName(tableName, entityName) + "  implements Serializable {\r\n\r\n");
        buffer.append(generateEntityAttrs(columnNames, columnTypes, columnComments));// 属性
        buffer.append("\r\n");
        buffer.append(generateGetAndSetMethod(columnNames, columnTypes));// get set方法
        buffer.append("}\r\n");
        //生成文件
        String fileDirectory = PROJECT_PATH + CLASSPATH + entityPath.replace(".", "/") + "/" + getEntityName(tableName, entityName) + ".java";
        generateFile(fileDirectory, buffer.toString());

    }

    /**
     * 获取实体名称
     * 如果指定了entityName，返回entityName
     * 否则对tableName进行驼峰命名转换并返回作为实体名称
     * @param tableName 表名
     * @param entityName 实体名
     * @return
     */
    private static String getEntityName(String tableName, String entityName) {
        if (StringUtil.isNotEmpty(entityName)) {
            return entityName.trim();
        } else {
            return StringUtil.transferToCamel(tableName, true);
        }
    }

    /**
     *  获取接口名称
      * @param tableName 表名
     * @param entityName 实体名
     * @param mapperName 接口名
     * @return
     */
    private static String getMapperName(String tableName, String entityName, String mapperName) {
        if (StringUtil.isNotEmpty(mapperName)) {
            return mapperName.trim();
        } else {
            return getEntityName(tableName, entityName) + "Dao";
        }
    }

    /**
     * 获取映射文件名称
     * @param tableName 表名
     * @param entityName 实体名
     * @param mappingName 映射文件名
     * @return
     */
    private static String getMappingName(String tableName, String entityName, String mappingName) {
        if (StringUtil.isNotEmpty(mappingName)) {
            return mappingName.trim();
        } else {
            return getEntityName(tableName, entityName) + "Mapper";
        }
    }

    /**
     * 生成实体属性
     * @param columnNames
     * @param columnTypes
     * @param columnComments
     * @return
     */
    private static String generateEntityAttrs(List<String> columnNames, List<String> columnTypes, List<String> columnComments) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < columnNames.size(); i++) {
            buffer.append("\tprivate " + sqlType2JavaType(columnTypes.get(i)) + " " + StringUtil.transferToCamel(columnNames.get(i), false) + ";//" + columnComments.get(i) + "\r\n");
        }
        return buffer.toString();
    }

    /**
     * 生成实体get set方法
     * @param columnNames
     * @param columnTypes
     * @return
     */
    private static String generateGetAndSetMethod(List<String> columnNames, List<String> columnTypes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < columnNames.size(); i++) {
            buffer.append("\tpublic void set" + StringUtil.transferToCamel(columnNames.get(i), true) + "(" + sqlType2JavaType(columnTypes.get(i)) + " "
                    + StringUtil.transferToCamel(columnNames.get(i), false) + "){\r\n");
            buffer.append("\t\tthis." + StringUtil.transferToCamel(columnNames.get(i), false) + "=" + StringUtil.transferToCamel(columnNames.get(i), false) + ";\r\n");
            buffer.append("\t}\r\n\r\n");
            buffer.append("\tpublic " + sqlType2JavaType(columnTypes.get(i)) + " get" + StringUtil.transferToCamel(columnNames.get(i), true) + "(){\r\n");
            buffer.append("\t\treturn " + StringUtil.transferToCamel(columnNames.get(i), false) + ";\r\n");
            buffer.append("\t}\r\n\r\n");
        }
        return buffer.toString();
    }

    private static boolean hasDateType(List<String> columnTypes) {
        boolean result = false;
        for (String columnType : columnTypes) {
            if (columnType.equalsIgnoreCase("datetime")) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 通过sql数据类型找到其对应的java数据类型
     *
     * @param sqlType 字段在数据库中类型
     * @return
     */
    private static String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint") || sqlType.equalsIgnoreCase("tinyint unsigned")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }
        return null;
    }

    /**
     * 生成类注释
     * @return
     */
    private static String generateClassComment() {
        StringBuffer buffer = new StringBuffer();
        // 类的注释部分
        buffer.append("/**\r\n");
        buffer.append(" * author name: " + AUTHOR_NAME + "\r\n");
        buffer.append(" * create time: " + DateUtil.DateToString(new Date(), DateUtil.DATE_TO_STRING_DETAIAL_PATTERN) + "\r\n");
        buffer.append(" */ \r\n");
        return buffer.toString();
    }

    /**
     * 生成文件
     * @param fileDirectory 文件保存路径
     * @param content 文件内容
     */
    private static void generateFile(String fileDirectory, String content) {
        File file = new File(fileDirectory);
        File fileParent = file.getParentFile();
        if(!fileParent.exists()) {
            fileParent.mkdirs();
        }
        try {
            FileWriter writer;
            file.createNewFile();
            writer = new FileWriter(fileDirectory);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*List<String> tableNames = new ArrayList<>();
        //将要生成模板代码的表名添加到集合中
        tableNames.add("t_role_company");*/
        List<String> tableNames = DatabaseHelper.getTableNames();
        if (null != tableNames && tableNames.size() > 0) {
            /*//实体名
            String entityName = "RoleCompany";
            //实体保存路径
            String entityPath = "com.sunning.bluesource.view.entity.system";
            //接口名
            String mapperName = "RoleCompanyDao";
            //接口保存路径
            String mapperPath = "com.sunning.bluesource.dao.system";
            //映射文件名称
            String mappingName = "RoleCompanyMapper";
            //映射文件保存路径
            String mappingPath = "mapper.system";
            //Service文件保存路径
            String servicePath = "com.sunning.bluesource.service.system";
            //Controller文件保存路径
            String controllerPath = "com.sunning.bluesource.controller.system";*/
            //实体名
            String entityName = "";
            //实体保存路径
            String entityPath = "";
            //接口名
            String mapperName = "";
            //接口保存路径
            String mapperPath = "";
            //映射文件名称
            String mappingName = "";
            //映射文件保存路径
            String mappingPath = "";
            //Service文件保存路径
            String servicePath = "";
            //Controller文件保存路径
            String controllerPath = "";
            generateEntity(tableNames, entityName,entityPath);
            generateMapper(tableNames, entityName, entityPath, mapperName,mapperPath);
            generateMapping(tableNames, entityName, entityPath, mapperPath,mapperName, mappingPath, mappingName);
            generateService(tableNames, entityName, entityPath, mapperName, mapperPath, servicePath);
            //generateController(tableNames, entityName, mapperName, mapperPath, servicePath,controllerPath);
        }
    }

}
