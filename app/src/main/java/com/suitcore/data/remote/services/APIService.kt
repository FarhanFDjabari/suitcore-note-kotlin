package com.suitcore.data.remote.services

import com.suitcore.data.model.Note
import com.suitcore.data.model.NoteRequest
import com.suitcore.data.model.Place
import com.suitcore.data.model.User
import com.suitcore.data.remote.wrapper.MapBoxResults
import com.suitcore.data.remote.wrapper.Result
import com.suitcore.data.remote.wrapper.Results
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by DODYDMW19 on 8/3/2017.
 */

interface APIService {

    @GET("users")
    fun getMembers(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int): Single<Results<User>>

    @GET("users")
    fun getMembersCoroutinesAsync(
            @Query("per_page") perPage: Int,
            @Query("page") page: Int): Deferred<Results<User>>

    @GET("notes")
    fun getNotes(): Deferred<Results<Note>>

    @GET("notes/{id}")
    fun getNoteById(@Path("id") id: String?): Deferred<Result<Note>>

    @POST("notes")
    fun postNewNote(
        @Body note: NoteRequest
    ) : Deferred<Result<Any>>

    @PUT("notes/{id}")
    fun editNoteById(
        @Path("id") id: String?,
        @Body note: NoteRequest
    ) : Deferred<Result<String?>>

    @DELETE("notes/{id}")
    fun deleteNoteById(
        @Path("id") id: String?
    ) : Deferred<Result<String?>>
}
