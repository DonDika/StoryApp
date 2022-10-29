package com.dondika.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg storyEntity: StoryEntity)

    @Query("SELECT * FROM story_db")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_db")
    fun deleteAll()

}