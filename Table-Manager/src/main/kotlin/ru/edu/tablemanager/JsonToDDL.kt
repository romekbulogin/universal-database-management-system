package ru.edu.tablemanager

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class JsonToDDL {
    fun parse(tableName: String, json: String): String {
        val factory = JsonFactory()
        val sql = StringBuilder(256)

        sql.append("CREATE TABLE ").append(tableName).append(" ( ");
        val mapper = ObjectMapper(factory)

        var rootNode: JsonNode? = null
        try {
            rootNode = mapper.readTree(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val fieldsIterator = rootNode!!.fields()
        while (fieldsIterator.hasNext()) {
            val (key, value) = fieldsIterator.next()
            println("Key: $key\tValue:$value")
            sql.append(key)
            if (value.canConvertToInt()) {
                sql.append(" INT, ")
            } else if (value.canConvertToLong()) {
                sql.append(" LONG, ")
            } else if (value.asText().contains("/")) {
                sql.append(" DATE, ")
            } else if (value.asText().contains("-")) {
                sql.append(" DATE, ")
            } else if (value.asText().length > 25) {
                sql.append(" VARCHAR( ").append(value.asText().length + 25).append("), ")
            } else {
                sql.append(" VARCHAR(25), ")
            }
        }
        sql.deleteCharAt(sql.length - 2);
        sql.append(" ) ");
        return sql.toString()
    }
}