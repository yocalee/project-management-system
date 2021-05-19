package com.scalefocus.repository;

/**
 * the copyright for the current class belongs to David Capka
 */
public class Query {
    private StringBuilder query;

    public Query delete(String table) {
        query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append(table);
        return this;
    }

    public Query where(String requirement) {
        query.append(" WHERE ");
        query.append(requirement);
        return this;
    }

    public Query from(String table) {
        query.append(" FROM ");
        query.append(table);
        return this;
    }

    public Query update(String table) {
        query = new StringBuilder();
        query.append("UPDATE ");
        query.append(table);
        query.append(" SET ");
        return this;
    }

    public Query set(String[] columns, Object[] values) throws IllegalArgumentException {
        if (columns.length != values.length || columns.length == 0) {
            throw new IllegalArgumentException("Invalid argument count");
        }
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            query.append(" = ");
            if (values[i].getClass() == String.class) {
                query.append("'");
                query.append(values[i]);
                query.append("'");
            }else{
                query.append(values[i]);
            }
            query.append(",");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        return this;
    }

    public Query insert(String table) {
        query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(table);
        return this;
    }

    public Query params(String[] params) {
        query.append(" (");
        for (String param : params) {
            query.append(param);
            query.append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(") ");
        return this;
    }

    public Query values(Object[] params) {
        query.append(" VALUES(");

        int count = params.length;

        if (count == 0)
            throw new IllegalArgumentException("Invalid parameter count");

        for (int i = 0; i < count; i++) {
            if (params[i].getClass() == String.class) {
                query.append("'");
                query.append(params[i]);
                query.append("'");
            } else {
                query.append(params[i]);
            }
            query.append(",");
        }

        query.deleteCharAt(query.lastIndexOf(","));
        query.append(");");
        return this;
    }

    public Query createTable(String tableName, String[] columns, String[] values){
        if (columns.length != values.length || columns.length == 0) {
            throw new IllegalArgumentException("Invalid argument count");
        }
        query = new StringBuilder();
        query.append(String.format("CREATE TABLE %s ( ", tableName));

        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            query.append(" ");
            query.append(values[i]);
            query.append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(", "));
        query.append(");");
        return this;
    }

    public Query select(Object[] columns) {
        query = new StringBuilder();
        query.append("SELECT ");
        if (columns != null) {
            for (Object column : columns) {
                query.append(column);
                query.append(",");
            }
            query.deleteCharAt(query.lastIndexOf(","));
        } else
            query.append("*");

        return this;
    }

    public String getQuery() {
        return query.toString();
    }
}
