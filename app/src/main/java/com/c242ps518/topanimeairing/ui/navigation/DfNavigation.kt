package com.c242ps518.topanimeairing.ui.navigation

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.reflect.Method

object NavigateDf {
    @Composable
    fun loadDF(
        className: String,
        methodName: String,
        objectInstance: Any = Any(),
        navigateToDetail: (Long) -> Unit,
        navigateBack: () -> Unit,
    ): Boolean {
        val dfClass = loadClassByReflection(className)
        Log.d("loadDF", "dfClass: $dfClass")
        if (dfClass != null) {
            val composer = currentComposer
            val method = findMethodByReflection(
                dfClass,
                methodName
            )
            Log.d("loadDF", "method: $method")
            if (method != null) {
                val isMethodInvoked = invokeMethod(
                    method,
                    objectInstance,
                    navigateToDetail,
                    navigateBack,
                    composer,
                    0
                )
                if (!isMethodInvoked) {
                    ShowDFNotFoundScreen()
                    return false
                }
                return true
            } else {
                ShowDFNotFoundScreen()
                return false
            }
        } else {
            ShowDFNotFoundScreen()
            return false
        }
    }

    fun findMethodByReflection(classMethod: Class<*>?, methodName: String): Method? {
        return try {
            if (!TextUtils.isEmpty(methodName)) {
                classMethod?.let { clazz ->
                    clazz.methods.find {
                        it.name.equals(methodName) && java.lang.reflect.Modifier.isStatic(
                            it.modifiers
                        )
                    }
                } ?: run {
                    null
                }
            } else {
                null
            }
        } catch (e: Throwable) {
            null
        }
    }

    fun loadClassByReflection(className: String): Class<*>? {
        return try {
            val classLoader = ::loadClassByReflection.javaClass.classLoader
            classLoader?.loadClass(className)
        } catch (e: Throwable) {
            null
        }
    }

    fun invokeMethod(method: Method, obj: Any, vararg args: Any): Boolean {
        return try {
            method.invoke(obj, *(args))
            true
        } catch (e: Throwable) {
            false
        }
    }


    @Composable
    fun ShowDFNotFoundScreen() {
        Column(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "dynamic feature not found",
                color = Color.White,
                modifier = Modifier.padding(all = 20.dp)
            )
        }
    }
}