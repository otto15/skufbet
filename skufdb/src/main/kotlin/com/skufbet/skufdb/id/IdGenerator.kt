package com.skufbet.skufdb.id

interface IdGenerator<T> {
    fun generate(): T
}
