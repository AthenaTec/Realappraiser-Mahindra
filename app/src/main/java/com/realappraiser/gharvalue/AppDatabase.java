package com.realappraiser.gharvalue;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import com.realappraiser.gharvalue.Interface.CaseDetailDAO;
import com.realappraiser.gharvalue.Interface.InterfaceCaseQuery;
import com.realappraiser.gharvalue.Interface.InterfaceDataModelQuery;
import com.realappraiser.gharvalue.Interface.InterfaceDocumentListQuery;
import com.realappraiser.gharvalue.Interface.InterfaceGetPhotoMeasurmentQuery;
import com.realappraiser.gharvalue.Interface.InterfaceGetPhotoQuery;
import com.realappraiser.gharvalue.Interface.InterfaceIndPropertyFloorsQuery;

import com.realappraiser.gharvalue.Interface.InterfaceIndPropertyFloorsValuationQuery;
import com.realappraiser.gharvalue.Interface.InterfaceIndPropertyValuationQuery;
import com.realappraiser.gharvalue.Interface.InterfaceIndpropertyQuery;
import com.realappraiser.gharvalue.Interface.InterfaceLatLongQuery;
import com.realappraiser.gharvalue.Interface.InterfaceOfflineCaseQuery;
import com.realappraiser.gharvalue.Interface.InterfaceOfflineDataModelQuery;
import com.realappraiser.gharvalue.Interface.InterfacePropertyQuery;
import com.realappraiser.gharvalue.Interface.InterfaceProximityQuery;
import com.realappraiser.gharvalue.Interface.PropertyUpdateCategory;
import com.realappraiser.gharvalue.Interface.TypeofPropertyQuery;
import com.realappraiser.gharvalue.communicator.DataModel;
import com.realappraiser.gharvalue.model.Case;
import com.realappraiser.gharvalue.model.CaseDetail;
import com.realappraiser.gharvalue.model.Document_list;
import com.realappraiser.gharvalue.model.GetPhoto;
import com.realappraiser.gharvalue.model.GetPhoto_measurment;
import com.realappraiser.gharvalue.model.IndProperty;
import com.realappraiser.gharvalue.model.IndPropertyFloor;
import com.realappraiser.gharvalue.model.IndPropertyFloorsValuation;
import com.realappraiser.gharvalue.model.IndPropertyValuation;
import com.realappraiser.gharvalue.model.LatLongDetails;
import com.realappraiser.gharvalue.model.OfflineDataModel;
import com.realappraiser.gharvalue.model.OflineCase;
import com.realappraiser.gharvalue.model.Property;
import com.realappraiser.gharvalue.model.PropertyUpdateRoomDB;
import com.realappraiser.gharvalue.model.Proximity;
import com.realappraiser.gharvalue.model.TypeOfProperty;

import org.jetbrains.annotations.NotNull;

/**
 * Created by user on 15-02-2018.
 */

@Database(entities = {DataModel.class, OfflineDataModel.class, TypeOfProperty.class, PropertyUpdateRoomDB.class, Case.class, Property.class, IndProperty.class, IndPropertyValuation.class, IndPropertyFloor.class, IndPropertyFloorsValuation.class, Proximity.class, GetPhoto.class, CaseDetail.class, OflineCase.class, Document_list.class, LatLongDetails.class, GetPhoto_measurment.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    // DataModal
    public abstract InterfaceDataModelQuery interfaceDataModelQuery();

    // DataModal
    public abstract InterfaceOfflineDataModelQuery interfaceOfflineDataModelQuery();

    // Case
    public abstract InterfaceCaseQuery interfaceCaseQuery();

    // Property
    public abstract InterfacePropertyQuery interfacePropertyQuery();

    // IndProperty
    public abstract InterfaceIndpropertyQuery interfaceIndpropertyQuery();

    // IndPropertyValuation
    public abstract InterfaceIndPropertyValuationQuery interfaceIndPropertyValuationQuery();

    // IndPropertyFloor
    public abstract InterfaceIndPropertyFloorsQuery interfaceIndPropertyFloorsQuery();

    // IndPropertyFloorsValuation
    public abstract InterfaceIndPropertyFloorsValuationQuery interfaceIndPropertyFloorsValuationQuery();

    // Proximity
    public abstract InterfaceProximityQuery interfaceProximityQuery();

    // GetPhoto
    public abstract InterfaceGetPhotoQuery interfaceGetPhotoQuery();

    // Offlinecase
    public abstract InterfaceOfflineCaseQuery interfaceOfflineCaseQuery();

    // DocumentList
    public abstract InterfaceDocumentListQuery interfaceDocumentListQuery();

    // LatLong
    public abstract InterfaceLatLongQuery interfaceLatLongQuery();

    // typeofproperty
    public abstract TypeofPropertyQuery typeofPropertyQuery();

    // Total Case
    public abstract CaseDetailDAO daoAccess();

    // property Update category
    public abstract PropertyUpdateCategory propertyUpdateCategory();

    // GetPhotoMeasurment
    public abstract InterfaceGetPhotoMeasurmentQuery interfaceGetPhotoMeasurmentQuery();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "RA-Database.db")
                    // allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    .allowMainThreadQueries()
                    /*.addMigrations(new Migration(1,2) {
                        @Override
                        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
                            database.execSQL("ALTER TABLE GetPhotoModel "
                                    +"ADD COLUMN Filename TEXT");
                        }
                    })*/.addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_5_6, MIGRATION_6_7,MIGRATION_7_8)
                    /* call fallbackToDestructiveMigration in the builder in which case Room will re-create all of the tables */.fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GetPhotoModel " + "ADD COLUMN Filename TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE GetPhotoModel "
//                    +"ADD COLUMN Filename TEXT");

            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerActEast TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerActWest TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerActNorth TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerActSouth TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerDocEast TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerDocWest TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerDocNorth TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN UnitBoundryPerDocSouth TEXT;");
        }
    };


    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE GetPhotoModel "
//                    +"ADD COLUMN Filename TEXT");

            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN HabitationPercentageinLocality TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN AdverseFeatureNearby TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN NameofOccupant TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN CoastalRegulatoryZone TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN CycloneZone TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN SeismicZone TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN FloodProneZone TEXT;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN IsInHillSlope INTEGER;");

            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN TypeofMasonryId INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN TypeofMortarId INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN ConcreteGrade TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN WhetherExpansionJointAvailable INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN IsProjectedPartAvailable INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN TypeofFootingFoundationId INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN TypeofSteelGradeId INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN EnvironmentExposureCondition TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN IsConstructionDoneasPerSanctionedPlan INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN TypeofSeller TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN ArchitectEngineerLicenseNo TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN NumberofMultiplekitchens TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN GroundCoverage TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN PlanAspectRatio TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN NoofFloorsAboveGround TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN BasementDetails TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN Basement INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN FireExit TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN GroundSlope TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN SoilType TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN IsSoilLiquefiable INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN DescriptionofConstructionStage TEXT;");

            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN IsEstimateJustified INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN IsAddProposedValuationPercenatge INTEGER;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN PercentageofEstimate TEXT;");

            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN EstimatedCostofConstructiononCompletion TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN LoanAmountInclusiveInsuranceOtherAmount TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN OwnContributionAmount TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN RecommendationPercentage TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN AmounttobeDisbursement TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN AmountSpent TEXT;");
            database.execSQL("ALTER TABLE IndPropertyValuationModal ADD COLUMN AverageConstructionPercentage TEXT;");


            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActEast TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActWest TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActNorth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActSouth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocEast TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocWest TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocNorth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocSouth TEXT;");

            database.execSQL("ALTER TABLE DataModel ADD COLUMN BankReferenceNo TEXT;");
            database.execSQL("ALTER TABLE OfflineDataModel ADD COLUMN BankReferenceNo TEXT;");
            database.execSQL("ALTER TABLE IndPropertyFloorModal ADD COLUMN flatPoojaNo INTEGER;");

        }
    };
/*
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE GetPhotoModel "
//                    +"ADD COLUMN Filename TEXT");

            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActEast TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActWest TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActNorth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerActSouth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocEast TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocWest TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocNorth TEXT;");
            database.execSQL("ALTER TABLE IndPropertyModal ADD COLUMN UnitBoundryPerDocSouth TEXT;");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE GetPhotoModel "
//                    +"ADD COLUMN Filename TEXT");

            database.execSQL("ALTER TABLE DataModel ADD COLUMN BankReferenceNo TEXT;");
            database.execSQL("ALTER TABLE OfflineDataModel ADD COLUMN BankReferenceNo TEXT;");
            database.execSQL("ALTER TABLE IndPropertyFloorModal ADD COLUMN flatPoojaNo INTEGER;");

        }
    };*/


    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DataModel ADD COLUMN UniqueIDofthevaluation TEXT;");
            database.execSQL("ALTER TABLE OfflineDataModel ADD COLUMN UniqueIDofthevaluation TEXT;");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE CaseModal ADD COLUMN SiteVisitDate TEXT;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN ConcreteGradeDd INTEGER;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN EnvironmentExposureConditionDd INTEGER;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN IsLiftInBuilding INTEGER;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN SoilTypeDd INTEGER;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN IsFireExit INTEGER;");
            database.execSQL("ALTER TABLE IndProperty ADD COLUMN IsGroundSlope INTEGER;");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE CaseModal ADD COLUMN ArchitectEngineerLicenseNo TEXT;");
            database.execSQL("ALTER TABLE CaseModal ADD COLUMN TypeofOwnershipDd INTEGER;");
            database.execSQL("ALTER TABLE PropertyModal ADD COLUMN IsConstructionDoneasPerSanctionedPlan INTEGER;");
        }
    };

}
