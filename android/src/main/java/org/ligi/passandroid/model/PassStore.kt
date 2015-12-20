package org.ligi.passandroid.model

import java.io.File

interface PassStore {

    fun save(pass: Pass)

    fun deleteCacheForId(id: String)

    fun getPassbookForId(id: String): Pass?

    fun deletePassWithId(id: String): Boolean

    fun getPathForID(id: String): File

    val passMap: Map<String,Pass>

    var currentPass: Pass?

    val classifier: PassClassifier

    fun notifyChange()
}
