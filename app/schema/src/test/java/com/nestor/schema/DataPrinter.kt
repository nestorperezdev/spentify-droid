package com.nestor.schema

import java.lang.reflect.Field
import java.util.Date

/**
 * From a DataClass will generate an string with all the required fields for create an instance of it.
 * That generated string can be pasted into the compiler to generate the corresponding class with the same
 * values.
 */
fun getStringInstanceOfData(instance: Any): String {
    val fields = instance::class.java.declaredFields
    val className = getClassName(instance)
    val sb = StringBuilder()
    sb.append(className).append("(")    // DataClass(
    if (!fields.isEmpty()) {
        var prefix = ""
        fields.forEach { field ->
            sb.append(prefix)
            sb.appendLine()
            sb.append("\t")
            sb.append(getFieldNameAndValue(instance, field))
            prefix = ","
        }
        sb.appendLine()
    }
    sb.append(")")
    return sb.toString()
}

private fun getFieldNameAndValue(instance: Any, field: Field): String {
    val sb = StringBuilder()
    field.isAccessible = true
    sb.append(field.name).append(" = ")
    sb.append(getFieldValue(field.get(instance)))
    return sb.toString()
}

private fun getClassName(instance: Any): String {
    return instance::class.java.name.replace("$", ".")
}

/**
 * field can have different types, if it's a primitive type it can be returned directly, if it's a
 * complex type we need to pass it to the function getStringInstanceOfData to get the string representation of it.
 * if it's a list we need to return listOf() with the elements of the list.
 * if it's null we need to return null.
 * if it's a string we need to return "value".
 */
private fun getFieldValue(value: Any?): String {
    return when (value) {
        null -> "null"
        //  escape special characters such as \n, \t, \r, \b, \f, \", \', \\
        is String -> "\"${
            value.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t")
                .replace("\r", "\\r").replace("\b", "\\b")
                .replace("\"", "\\\"").replace("\'", "\\\'")
        }\""

        is Number, is Boolean -> value.toString()
        is List<*> -> getListOfValues(value)
        is Date -> "Date(" + value.time + ")"
        is Enum<*> -> getClassName(value) + "." + value.name
        else -> getStringInstanceOfData(value)
    }
}


private fun getListOfValues(list: List<*>): String {
    val sb = StringBuilder()
    sb.append("listOf(")
    if (!list.isEmpty()) {
        var prefix = ""
        list.forEach { element ->
            sb.append(prefix)
            sb.append(getFieldValue(element))
            prefix = ","
        }
    }
    sb.append(")")
    return sb.toString()
}

