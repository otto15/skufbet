package com.skufbet.utils.database.id

interface IdGenerator<T> {
    fun generate(): T
}
