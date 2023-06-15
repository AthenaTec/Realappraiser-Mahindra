package com.realappraiser.gharvalue.Interface;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.realappraiser.gharvalue.communicator.DataModel;

import java.util.List;

/**
 * Created by user on 15-02-2018.
 */

@Dao
public interface InterfaceDataModelQuery {

    @Query("SELECT * FROM datamodel")
    List<DataModel> getDataModels();


    @Query("SELECT * FROM datamodel where opencase = (:is_open)")
    List<DataModel> getDataModal_opencase(boolean is_open);

    @Query("SELECT * FROM datamodel where offlinecase = (:is_offline) AND opencase = (:is_open)")
    List<DataModel> getDataModal_offlinecase(boolean is_offline, boolean is_open);


    @Query("SELECT COUNT(*) from datamodel")
    int countdata();

    @Insert
    void insertAll(DataModel dataModel);

    @Update
    void updateData(DataModel dataModel);


    @Query("update dataModel set offlinecase=:isoffline where caseid in(:caseidIn)")
    void updateOfflineDataModel(String caseidIn, boolean isoffline);


    @Delete
    void delete(DataModel dataModel);

    @Query("DELETE FROM datamodel")
    public void deleteTable();

    @Query("SELECT * FROM datamodel where CaseId = (:CaseId)")
    List<DataModel> getCase_caseID(String CaseId);

    @Query("update datamodel set StatusId=:updatecasestatus where caseid in(:caseidIn)")
    void updateOnlineCaseStatusonline(String updatecasestatus, String caseidIn);

    // Delete all rows
    @Query("DELETE FROM datamodel")
    public void deleteRow();




}
