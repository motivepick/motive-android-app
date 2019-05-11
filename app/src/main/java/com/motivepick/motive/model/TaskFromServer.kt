package com.motivepick.motive.model

import java.util.*

data class TaskFromServer(val id: Long?, val name: String, val description: String?, val dueDate: Date?, val closed: Boolean)
