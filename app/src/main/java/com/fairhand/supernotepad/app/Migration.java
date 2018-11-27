package com.fairhand.supernotepad.app;

import android.support.annotation.NonNull;

import com.fairhand.supernotepad.util.Logger;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * 数据库迁移配置
 *
 * @author Phanton
 * @date 11/25/2018 - Sunday - 5:53 PM
 */
public class Migration implements RealmMigration {
    
    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        Logger.d("旧数据库版本信息：", "" + oldVersion);
        Logger.d("新数据库版本信息：", "" + newVersion);
        
        RealmSchema schema = realm.getSchema();
        
        //**************************************************
        //// Version 1
        //class RealmAffair                   // add a new model class
        //@Required private String title;
        //@Required private String time;
        //private String backup;
        //private int kindId;
        //private boolean remind;
        //
        //class RealmSecretAffair         // add a new model class
        //@Required private String title;
        //@Required private String time;
        //private String backup;
        //private int kindId;
        //private boolean remind;
        //***********************************************
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            // Create a new class
            schema.create("RealmAffair")
                    .addField("title", String.class, FieldAttribute.REQUIRED)
                    .addField("time", String.class, FieldAttribute.REQUIRED)
                    .addField("backup", String.class, (FieldAttribute) null)
                    .addField("kindId", int.class, (FieldAttribute) null)
                    .addField("remind", boolean.class, (FieldAttribute) null);
            // Create a new class
            schema.create("RealmSecretAffair")
                    .addField("title", String.class, FieldAttribute.REQUIRED)
                    .addField("time", String.class, FieldAttribute.REQUIRED)
                    .addField("backup", String.class, (FieldAttribute) null)
                    .addField("kindId", int.class, (FieldAttribute) null)
                    .addField("remind", boolean.class, (FieldAttribute) null);
            oldVersion++;
        }
        //**************************************************
        //// Version 2
        //class RealmAffair
        // private String key;               // add a new field
        //
        //class RealmSecretAffair
        //private String key;                   // add a new field
        //***********************************************
        // Migrate from version 1 to version 2
        if (oldVersion == 1) {
            Objects.requireNonNull(schema.get("RealmAffair"))
                    .addField("key", String.class, (FieldAttribute) null);
            Objects.requireNonNull(schema.get("RealmSecretAffair"))
                    .addField("key", String.class, (FieldAttribute) null);
            oldVersion++;
        }
        //**************************************************
        //// Version 3
        //class RealmAffair
        // private String kindName;               // add a new field
        // private int kindIcon;                      // add a new field
        //                 kindId                           // remove a old field
        //
        //class RealmSecretAffair
        // private String kindName;               // add a new field
        // private int kindIcon;                      // add a new field
        //                 kindId                           // remove a old field
        //***********************************************
        // Migrate from version 2 to version 3
        if (oldVersion == 2) {
            Objects.requireNonNull(schema.get("RealmAffair"))
                    .removeField("kindId")
                    .addField("kindName", String.class, (FieldAttribute) null)
                    .addField("kindIcon", int.class, (FieldAttribute) null);
            Objects.requireNonNull(schema.get("RealmSecretAffair"))
                    .removeField("kindId")
                    .addField("kindName", String.class, (FieldAttribute) null)
                    .addField("kindIcon", int.class, (FieldAttribute) null);
        }
    }
    
}
