package com.motivepick.motive

data class UpdateTaskRequest(val name: String?, val deleteDueDate: Boolean) {

    constructor(name: String?) : this(name, false)

    constructor(deleteDueDate: Boolean) : this(null, deleteDueDate)
}
