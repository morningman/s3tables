package com.example;

import org.apache.iceberg.FileScanTask;
import org.apache.iceberg.Table;
import org.apache.iceberg.TableScan;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.util.TableScanUtil;
import software.amazon.s3tables.iceberg.S3TablesCatalog;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class S3TablesDemo {

    public static void main(String[] args) {
        System.out.println("AWS Region: " + System.getenv("AWS_REGION"));
        System.out.println("AWS Access Key ID: " + System.getenv("AWS_ACCESS_KEY_ID"));
        
        S3TablesCatalog s3TablesCatalog = new S3TablesCatalog();
        Map<String, String> s3Properties = new HashMap<>();
        
        // 创建自定义凭证提供程序
        String accessKeyId = "";
        String secretKey = "";

        // s3Properties.put("s3.region", "us-east-1");
        s3Properties.put("client.region", "us-east-1");
        s3Properties.put("client.credentials-provider", CustomAwsCredentialsProvider.class.getName());
        s3Properties.put("client.credentials-provider.s3.access-key-id", accessKeyId);
        s3Properties.put("client.credentials-provider.s3.secret-access-key", secretKey);
        
        String warehouse = "arn:aws:s3tables:us-east-1:169698404049:bucket/yy-s3-table-bucket";
        s3Properties.put("warehouse", warehouse);

        try {
            s3TablesCatalog.initialize("s3tables", s3Properties);
            System.out.println("Successfully initialized S3 Tables catalog!");
            
            // // 尝试创建一个命名空间
            // try {
            //     Namespace namespace = Namespace.of("test_namespace");
            //     s3TablesCatalog.createNamespace(namespace);
            //     System.out.println("Successfully created namespace: " + namespace);
            // } catch (Exception e) {
            //     e.printStackTrace();
            //     System.out.println("Note: Could not create namespace - " + e.getMessage());
            // }

            // 尝试列出命名空间
            try {
                List<Namespace> namespaces = s3TablesCatalog.listNamespaces();
                System.out.println("Successfully listed namespaces:");
                for (Namespace namespace : namespaces) {
                    System.out.println(namespace);
                    List<TableIdentifier> tblIdentifiers = s3TablesCatalog.listTables(namespace);
                    for (TableIdentifier tblId : tblIdentifiers) {
                        System.out.println(tblId);
                        Table tbl = s3TablesCatalog.loadTable(tblId);
                        System.out.println(tbl.schema());
                        TableScan scan = tbl.newScan();
                        CloseableIterable<FileScanTask> fileScanTasks = scan.planFiles();
                        for (FileScanTask task : fileScanTasks) {
                            System.out.println(task.file());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Note: Could not list namespaces - " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error connecting to S3 Tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
