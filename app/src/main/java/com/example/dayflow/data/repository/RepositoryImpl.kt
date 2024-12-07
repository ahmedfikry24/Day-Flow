package com.example.dayflow.data.repository

import com.example.dayflow.data.local.LocalDataBase
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataBase: LocalDataBase,
) : Repository {
}
