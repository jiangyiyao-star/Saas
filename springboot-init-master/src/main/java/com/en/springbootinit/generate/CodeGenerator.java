package com.en.springbootinit.generate;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 进阶版代码生成器：直连数据库，读取字段信息生成代码
 */
public class CodeGenerator {

    // ================== 1. 数据库配置信息 (请在这里修改) ==================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/saas?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";


    public static void main(String[] args) throws Exception {
        // ================== 2. 生成参数配置 ==================
        String tableName = "device_info";          // 数据库表名
        String packageName = "com.en.springbootinit";
        String dataName = "设备类型";               // 业务中文名
        String dataKey = "deviceType";             // 实例变量名 (首字母小写)
        String upperDataKey = "DeviceType";        // 类名 (首字母大写)

        // ================== 3. 读取数据库表结构 ==================
        List<ColumnInfo> columns = getTableColumns(tableName);
        if (columns.isEmpty()) {
            System.err.println("未找到表 " + tableName + " 的字段信息，请检查表名或数据库连接！");
            return;
        }

        // ================== 4. 封装 FreeMarker 数据模型 ==================
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);
        dataModel.put("columns", columns);

        // ================== 5. 执行生成逻辑 ==================
        // ⭐ 核心修改：在 user.dir 后面加上真实的子模块文件夹名
        String projectPath = System.getProperty("user.dir") + "/springboot-init-master";

        // 现在再往下拼接 src 就能精准命中了
        String templateDirPath = projectPath + "/src/main/resources/templates/model";
        String templateName = "TemplateVO.java.ftl";
        String outputPath = projectPath + "/generator/model/vo/" + upperDataKey + "VO.java";

        doGenerate(templateDirPath, templateName, outputPath, dataModel);
        System.out.println("🎉 生成 VO 成功，文件路径：" + outputPath);
    }


    /**
     * 连接数据库并获取表的所有列信息
     */
    private static List<ColumnInfo> getTableColumns(String tableName) throws Exception {
        List<ColumnInfo> columns = new ArrayList<>();
        // 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            DatabaseMetaData metaData = conn.getMetaData();
            // 获取列信息：catalog, schemaPattern, tableNamePattern, columnNamePattern
            ResultSet rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);

            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String dbType = rs.getString("TYPE_NAME");
                String remarks = rs.getString("REMARKS");

                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(columnName);
                columnInfo.setColumnComment(remarks != null ? remarks : "");

                // 将数据库下划线命名转为 Java 驼峰命名 (如 device_sn -> deviceSn)
                columnInfo.setPropertyName(underlineToCamel(columnName));
                // 将数据库类型转为 Java 类型 (如 VARCHAR -> String)
                columnInfo.setJavaType(convertToJavaType(dbType));

                columns.add(columnInfo);
            }
        }
        return columns;
    }

    /**
     * 数据库类型映射到 Java 类型
     */
    private static String convertToJavaType(String dbType) {
        dbType = dbType.toUpperCase();
        if (dbType.contains("VARCHAR") || dbType.contains("TEXT") || dbType.contains("CHAR") || dbType.contains("JSON")) {
            return "String";
        } else if (dbType.contains("BIGINT")) {
            return "Long";
        } else if (dbType.contains("INT") || dbType.contains("TINYINT")) {
            return "Integer";
        } else if (dbType.contains("DECIMAL") || dbType.contains("NUMERIC")) {
            return "BigDecimal";
        } else if (dbType.contains("DATETIME") || dbType.contains("TIMESTAMP") || dbType.contains("DATE")) {
            return "Date";
        }
        return "Object"; // 默认兜底类型
    }

    /**
     * 下划线转驼峰 (例如: user_name -> userName)
     */
    private static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 进阶版：执行 FreeMarker 模板生成（附带路径诊断）
     */
    public static void doGenerate(String templateDirPath, String templateName, String outputPath, Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

        // 构建模板目录文件对象
        File templateDir = new File(templateDirPath);

        // 🚨 诊断拦截：如果这里还说不存在，把真实去找的路径打印出来，一眼就能看出哪里拼错了
        if (!templateDir.exists()) {
            System.err.println("【严重错误】Java 在物理磁盘上找不到这个目录！");
            System.err.println("Java 试图去寻找的路径是: " + templateDir.getAbsolutePath());
            System.err.println("请对比上面的路径和你项目真实的物理路径是否完全一致。");
            return;
        }

        // 设置模板目录
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("utf-8");

        // 获取模板文件
        Template template = configuration.getTemplate(templateName);

        // 文件不存在则创建文件和父目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        // 写入生成
        try (Writer out = new FileWriter(outputPath)) {
            template.process(model, out);
        }

    }

    /**
     * 内部类：用于封装列信息，传递给 FreeMarker
     */
    public static class ColumnInfo {
        private String columnName;    // 数据库列名，如 device_sn
        private String propertyName;  // Java 属性名，如 deviceSn
        private String javaType;      // Java 类型，如 String
        private String columnComment; // 字段注释，如 "设备序列号"

        // Getters and Setters (FreeMarker 需要 Get 方法来读取属性)
        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public String getColumnComment() {
            return columnComment;
        }

        public void setColumnComment(String columnComment) {
            this.columnComment = columnComment;
        }
    }
}