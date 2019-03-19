package com.motivepick.motive

data class UpdateTaskRequest(val name: String?, val description: String?, val deleteDueDate: Boolean) {

    constructor(name: String?) : this(name, null, false)

    constructor(name: String?, description: String?) : this(name, description, false)

    constructor(deleteDueDate: Boolean) : this(null, null, deleteDueDate)
}
