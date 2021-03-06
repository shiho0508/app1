package com.example.app1.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.app1.model.Post;
import com.example.app1.model.Profile;

@Database(entities = {Profile.class, Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfileDao profileDao();
    public abstract PostDao postDao();
}