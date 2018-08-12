package com.fizal.it;

import com.datastax.driver.core.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple utility program to read Cassandra DB data
 * <p>
 * Created by Fizal on 8/12/18.
 */
public class CassandraUtil {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint("localhost").withPort(4000).build();

        try {
            List<String> keyspaces = getKeyspaces(cluster);
            System.out.println(keyspaces);

            for (String keyspace : keyspaces) {
                System.out.println("Tables from keyspace: " + keyspace);
                List<String> tableNames = getTableNames(cluster, keyspace);
                System.out.println(tableNames);

                for (String tableName : tableNames) {
                    printRows(cluster, keyspace, tableName);
                }
            }

        } finally {
            cluster.close();
        }
    }

    private static List<String> getKeyspaces(Cluster cluster) {
        return cluster
                .getMetadata()
                .getKeyspaces()
                .stream()
                .map(KeyspaceMetadata::getName)
                .collect(Collectors.toList());
    }

    private static List<String> getTableNames(Cluster cluster, String keyspace) {
        return cluster
                .getMetadata()
                .getKeyspace(keyspace)
                .getTables()
                .stream()
                .map(AbstractTableMetadata::getName)
                .collect(Collectors.toList());
    }

    private static void printRows(Cluster cluster, String keyspace, String table) {
        try {
            ResultSet rs = cluster.connect(keyspace).execute("SELECT * FROM " + table);
            for (Row row : rs) {
                System.out.println(row);

                for (ColumnDefinitions.Definition col : row.getColumnDefinitions()) {
                    System.out.println("\tColumn name: " + col.getName()
                            + " type: " + col.getType()
                            + " value: " + row.getObject(col.getName())
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
