/*
 *
 *  * Copyright 2023 Hieu Luu (neo). All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.hieuluu.cardreader.framework.epoxy

import android.view.View
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import com.airbnb.epoxy.EpoxyModel
import com.hieuluu.cardreader.R
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Created by Hieu Luu (neo) on 08/06/2023
 * A pattern for using epoxy models with Kotlin with no annotations or code generation.
 *
 * See [com.airbnb.epoxy.kotlinsample.models.ItemViewBindingDataClass] for a usage example.
 *
 * If You use Proguard or R8, be sure to keep the bind method available with the following configuration:
 *
 * -keepclassmembers class * extends androidx.viewbinding.ViewBinding {
 *    public static *** bind(android.view.View);
 * }
 */
abstract class ViewBindingKotlinModel<T : ViewBinding>(
    @LayoutRes private val layoutRes: Int
) : EpoxyModel<View>() {
    // Using reflection to get the static binding method.
    // Lazy so it's computed only once by instance, when the 1st ViewHolder is actually created.
    private val bindingMethod by lazy { getBindMethodFrom(this::class.java) }

    abstract fun T.bind()

    @Suppress("UNCHECKED_CAST")
    override fun bind(view: View) {
        var binding = view.getTag(R.id.epoxy_viewbinding) as? T
        if (binding == null) {
            binding = bindingMethod.invoke(null, view) as T
            view.setTag(R.id.epoxy_viewbinding, binding)
        }
        binding.bind()
    }

    override fun getDefaultLayout(): Int = layoutRes
}

// Static cache of a method pointer for each type of item used.
private val sBindingMethodByClass = ConcurrentHashMap<Class<*>, Method>()

@Suppress("UNCHECKED_CAST")
@Synchronized
private fun getBindMethodFrom(javaClass: Class<*>): Method =
    sBindingMethodByClass.getOrPut(javaClass) {
        val actualTypeOfThis = getSuperclassParameterizedType(javaClass)
        val viewBindingClass = actualTypeOfThis.actualTypeArguments[0] as Class<ViewBinding>
        viewBindingClass.getDeclaredMethod("bind", View::class.java)
            ?: error("The binder class ${javaClass.canonicalName} should have a method bind(View)")
    }

private fun getSuperclassParameterizedType(klass: Class<*>): ParameterizedType {
    val genericSuperClass = klass.genericSuperclass
    return (genericSuperClass as? ParameterizedType)
        ?: getSuperclassParameterizedType(genericSuperClass as Class<*>)
}