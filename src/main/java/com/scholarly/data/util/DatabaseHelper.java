package com.scholarly.data.util;

import com.scholarly.MainApplication;
import com.scholarly.data.dao.NovelChapterDao;
import com.scholarly.data.dao.SubjectDao;
import com.scholarly.data.dao.newDb.SectionDao;
import com.scholarly.data.model.newDb.SubjectCombination;
import com.scholarly.util.AppProperties;
import com.scholarly.util.PreferencesManager;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import static com.scholarly.util.Constants.PREF_KEY_DB_VERSION;

public class DatabaseHelper {
    public static void initDb() {
        try {
            int dbVersion = AppProperties.getInstance().getDbVersion();
            int prevDbVersion = PreferencesManager.getInt(PREF_KEY_DB_VERSION, 1);

            if (dbVersion > prevDbVersion) {
                boolean migrated = migrateDb();

                if (migrated) {
                    PreferencesManager.putInt(PREF_KEY_DB_VERSION, dbVersion);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
        }
    }

    private static boolean migrateDb() {
        String dbName = AppProperties.getInstance().getDbName();
        String tempDbName = "temp_" + dbName;

        DatabaseBuilder oldDb = new DatabaseBuilder(dbName);
        DatabaseBuilder newDb = new DatabaseBuilder(tempDbName);

        Connection oldDbConn = oldDb.getConnection();
        Connection newDbConn = newDb.getConnection();

        try {
            ArrayList<TableInfo> tableInfo = new ArrayList<>() {
                {
                    add(
                            new TableInfo(
                                    Tables.SUBJECTS_COMBINATION,
                                    new ArrayList<>() {
                                        {
                                            add(new ColumnInfo(SubjectDao.idColumn, Types.INTEGER));
                                            add(new ColumnInfo(SubjectDao.subjectIdColumn, Types.INTEGER));
                                            add(new ColumnInfo(SubjectDao.userIdColumn, Types.VARCHAR));
                                        }
                                    }
                            )
                    );
                    add(
                            new TableInfo(
                                    Tables.NOTE_LAST_SESSION,
                                    new ArrayList<>() {
                                        {
                                            add(new ColumnInfo(SectionDao.idColumn, Types.INTEGER));
                                            add(new ColumnInfo(SectionDao.sectionIdColumn, Types.INTEGER));
                                            add(new ColumnInfo(SectionDao.sectionTitleColumn, Types.VARCHAR));
                                            add(new ColumnInfo(SectionDao.userIdColumn, Types.VARCHAR));
                                        }
                                    }
                            )
                    );
                    add(
                            new TableInfo(
                                    Tables.NOVEL_LAST_SESSION,
                                    new ArrayList<>() {
                                        {
                                            add(new ColumnInfo(NovelChapterDao.idColumn, Types.INTEGER));
                                            add(new ColumnInfo(NovelChapterDao.chapterIdColumn, Types.INTEGER));
                                            add(new ColumnInfo(NovelChapterDao.chapterTitleColumn, Types.VARCHAR));
                                            add(new ColumnInfo(NovelChapterDao.userIdColumn, Types.VARCHAR));
                                        }
                                    }
                            )
                    );
                }
            };
            boolean copied = true;
            for (TableInfo info: tableInfo) {
                copied = copied && copyData(oldDbConn, newDbConn, info);
            }

            if (copied) {
                String fileName = oldDb.getPath();
                boolean deleted = oldDb.deleteDb();

                if (deleted) {
                    File file = new File(fileName);
                    return newDb.renameDb(file);
                } else {
                    return false;
                }
            } else {
                newDb.deleteDb();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MainApplication.log(e);
            newDb.deleteDb();
            return false;
        }
    }

    private static Boolean copyData(Connection originalDatabase, Connection newDatabase, TableInfo tableInfo) throws SQLException {
        String query = "SELECT " +  String.join(", ", tableInfo.columns.stream().map(c -> c.columnName).toList()) + " FROM " + tableInfo.tableName;
        ResultSet rSet = originalDatabase.prepareStatement(query).executeQuery();

        while (rSet.next()) {
            int columnSize = tableInfo.columns.size();
            Object[] contents = new Object[columnSize];
            int[] types = new int[columnSize];

            for (int i = 0; i < tableInfo.columns.size(); i++) {
                ColumnInfo c = tableInfo.columns.get(i);
                Object content = ColumnInfo.getContent(rSet, c);

                contents[i] = content;
                types[i] = c.columnType;
            }

            String[] columns = new String[columnSize];
            tableInfo.columns.stream().map(c -> c.columnName).toList().toArray(columns);

            query = CRUDHelper.insertOrReplaceQuery(tableInfo.tableName, columns, contents, types);
            newDatabase.prepareStatement(query).executeUpdate();
        }
        return true;
    }

    record TableInfo(String tableName, ArrayList<ColumnInfo> columns) { }
    record ColumnInfo(String columnName, int columnType) {
        static Object getContent(ResultSet rs, ColumnInfo info) throws SQLException {
            return switch (info.columnType) {
                case Types.INTEGER -> rs.getInt(info.columnName);
                case Types.VARCHAR -> rs.getString(info.columnName);
                default -> null;
            };
        }
    }
}
