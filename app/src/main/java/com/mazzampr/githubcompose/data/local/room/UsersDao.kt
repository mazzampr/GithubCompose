package com.mazzampr.githubcompose.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mazzampr.githubcompose.data.local.entity.UsersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Query("SELECT * FROM users_fav")
    fun getLikedUsers(): Flow<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UsersEntity)

    @Delete
    suspend fun delete(user: UsersEntity)

    @Query("SELECT * from users_fav WHERE username = :username")
    fun getUserDetail(username: String): Flow<List<UsersEntity>>
}