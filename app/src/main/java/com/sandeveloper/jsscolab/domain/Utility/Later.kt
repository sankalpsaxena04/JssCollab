package com.sandeveloper.jsscolab.domain.Utility

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.EXPRESSION)
annotation class Later(val todo : String)
annotation class Version(val version : String)



