package com.dondika.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dondika.storyapp.data.local.room.RemoteKeysEntity
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.ApiService

@ExperimentalPagingApi
class StoryRemoteMediator(
    private val token: String,
    private val api: ApiService,
    private val database: StoryDatabase
) : RemoteMediator<Int, StoryEntity>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, StoryEntity>): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosetToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prefKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prefKey
            }
            LoadType.APPEND -> {
                val remoteKey = getremoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }

        try {
            val responseData = api.getAllStories("Bearer $token", page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else -1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val key = responseData.listStory.map {
                    RemoteKeysEntity(it.id, prevKey, nextKey)
                }
                database.remoteKeysDao().insertAll(key)
                responseData.listStory.forEach { listStoryItem ->
                    val storyData = StoryEntity(
                        listStoryItem.id,
                        listStoryItem.name,
                        listStoryItem.description,
                        listStoryItem.createdAt,
                        listStoryItem.photoUrl,
                        listStoryItem.lat,
                        listStoryItem.lon
                    )
                    database.storyDao().insertStory(storyData)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull{ it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getremoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosetToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }


}