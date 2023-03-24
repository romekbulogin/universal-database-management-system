package ru.edu.tablemanager.service

import org.jooq.DataType
import org.jooq.impl.SQLDataType

class SQLEnum {
    companion object {
        fun getSqlDataType(dataType: String): DataType<out Any>? {
            return when (dataType) {
                "VARCHAR" -> SQLDataType.VARCHAR
                "CHAR" -> SQLDataType.CHAR
                "LONGVARCHAR" -> SQLDataType.LONGVARCHAR
                "CLOB" -> SQLDataType.CLOB
                "NVARCHAR" -> SQLDataType.NVARCHAR
                "NCHAR" -> SQLDataType.NCHAR
                "LONGNVARCHAR" -> SQLDataType.LONGNVARCHAR
                "NCLOB" -> SQLDataType.NCLOB
                "BOOLEAN" -> SQLDataType.BOOLEAN
                "BIT" -> SQLDataType.BIT
                "TINYINT" -> SQLDataType.TINYINT
                "SMALLINT" -> SQLDataType.SMALLINT
                "INTEGER" -> SQLDataType.INTEGER
                "BIGINT" -> SQLDataType.BIGINT
                "DECIMAL_INTEGER" -> SQLDataType.DECIMAL_INTEGER
                "TINYINTUNSIGNED" -> SQLDataType.TINYINTUNSIGNED
                "SMALLINTUNSIGNED" -> SQLDataType.SMALLINTUNSIGNED
                "INTEGERUNSIGNED" -> SQLDataType.INTEGERUNSIGNED
                "BIGINTUNSIGNED" -> SQLDataType.BIGINTUNSIGNED
                "DOUBLE" -> SQLDataType.DOUBLE
                "FLOAT" -> SQLDataType.FLOAT
                "REAL" -> SQLDataType.REAL
                "NUMERIC" -> SQLDataType.NUMERIC
                "DECIMAL" -> SQLDataType.DECIMAL
                "DATE" -> SQLDataType.DATE
                "TIMESTAMP" -> SQLDataType.TIMESTAMP
                "TIME" -> SQLDataType.TIME
                "INTERVAL" -> SQLDataType.INTERVAL
                "INTERVALYEARTOMONTH" -> SQLDataType.INTERVALYEARTOMONTH
                "INTERVALDAYTOSECOND" -> SQLDataType.INTERVALDAYTOSECOND
                "LOCALTIME" -> SQLDataType.LOCALTIME
                "LOCALDATETIME" -> SQLDataType.LOCALDATETIME
                "OFFSETTIME" -> SQLDataType.OFFSETTIME
                "OFFSETDATETIME" -> SQLDataType.OFFSETDATETIME
                "TIMEWITHTIMEZONE" -> SQLDataType.TIMEWITHTIMEZONE
                "TIMESTAMPWITHTIMEZONE" -> SQLDataType.TIMESTAMPWITHTIMEZONE
                "INSTANT" -> SQLDataType.INSTANT
                "BINARY" -> SQLDataType.BINARY
                "VARBINARY" -> SQLDataType.VARBINARY
                "LONGVARBINARY" -> SQLDataType.LONGVARBINARY
                "BLOB" -> SQLDataType.BLOB
                "OTHER" -> SQLDataType.OTHER
                "ROWID" -> SQLDataType.ROWID
                "RECORD" -> SQLDataType.RECORD
                "RESULT" -> SQLDataType.RESULT
                "UUID" -> SQLDataType.UUID
                "JSON" -> SQLDataType.JSON
                "JSONB" -> SQLDataType.JSONB
                else -> null
            }
        }


        fun getSqlDataType(dataType: String, length: Int): DataType<*>? {
            return when (dataType) {
                "VARCHAR" -> SQLDataType.VARCHAR(length)
                "CHAR" -> SQLDataType.CHAR(length)
                "LONGVARCHAR" -> SQLDataType.LONGVARCHAR(length)
                "CLOB" -> SQLDataType.CLOB(length)
                "NVARCHAR" -> SQLDataType.NVARCHAR(length)
                "NCHAR" -> SQLDataType.NCHAR(length)
                "LONGNVARCHAR" -> SQLDataType.LONGNVARCHAR(length)
                "NCLOB" -> SQLDataType.NCLOB(length)
                "DECIMAL_INTEGER" -> SQLDataType.DECIMAL_INTEGER(length)
                "NUMERIC" -> SQLDataType.NUMERIC(length)
                "DECIMAL" -> SQLDataType.DECIMAL(length)
                "TIMESTAMP" -> SQLDataType.TIMESTAMP(length)
                "TIME" -> SQLDataType.TIME(length)
                "OFFSETTIME" -> SQLDataType.OFFSETTIME(length)
                "OFFSETDATETIME" -> SQLDataType.OFFSETDATETIME(length)
                "TIMEWITHTIMEZONE" -> SQLDataType.TIMEWITHTIMEZONE(length)
                "TIMESTAMPWITHTIMEZONE" -> SQLDataType.TIMESTAMPWITHTIMEZONE(length)
                "INSTANT" -> SQLDataType.INSTANT(length)
                "BINARY" -> SQLDataType.BINARY(length)
                "VARBINARY" -> SQLDataType.VARBINARY(length)
                "LONGVARBINARY" -> SQLDataType.LONGVARBINARY(length)
                "BLOB" -> SQLDataType.BLOB(length)
                else -> null
            }
        }
    }
}
