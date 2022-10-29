package com.dondika.storyapp.data.local.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "story_db")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "photo_url")
    val photoUrl: String,
    val lat: Double?,
    val lng: Double?

) : Parcelable
