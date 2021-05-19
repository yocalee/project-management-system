package com.scalefocus.service;

import com.scalefocus.model.BaseEntity;
import com.scalefocus.repository.DataRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<E extends BaseEntity> {
    private final DataRepository<E> dataRepository;

    protected AbstractService (DataRepository<E> dataRepository){
        this.dataRepository = dataRepository;
    }

    public List<E> findAll() {
        try {
            return dataRepository.findAll();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public E findById(Integer id) {
        try {
            return dataRepository.findById(id);
        } catch (SQLException e) {
            System.out.println("Invalid id.");
            return null;
        }
    }

    public E save(String[] columns, Object[] values) {
        try {
            return dataRepository.save(columns,values);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public E update(String[] columns, Object[] values, String requirement) {
        try {
            return dataRepository.update(columns, values, requirement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public E delete(int id) {
        try {
            return dataRepository.delete(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void saveInCommonTable(String tableName, String[] columns, Object[] values) {
        try {
            dataRepository.saveInCommonTable(tableName, columns, values);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteInCommonTable(String tableName, String requirements) {
        try{
            dataRepository.deleteInCommonTable(tableName, requirements);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateInCommonTable(String tableName, String[] columns, Object[] values, String requirement){
        try{
            dataRepository.updateInCommonTable(tableName, columns, values, requirement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<E> findInCommonTable(String tableName, String requirement, String columnId) {
        try {
            return dataRepository.findInCommonTable(tableName, requirement, columnId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
