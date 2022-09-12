package com.example.githubappsv3.Favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity (
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var id:Int,
    @ColumnInfo(name = "username")
    var username:String,
    @ColumnInfo(name = "avatar")
    var avatar:String
)