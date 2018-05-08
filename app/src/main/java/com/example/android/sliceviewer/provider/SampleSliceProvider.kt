/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sliceviewer.provider

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.core.SliceHints
import androidx.slice.core.SliceHints.ICON_IMAGE
import com.example.android.sliceviewer.R

class SampleSliceProvider : SliceProvider() {
    override fun onBindSlice(sliceUri: Uri?): Slice? {
        if (sliceUri == null || sliceUri.path == null) {
            return null
        }
        return when(sliceUri.path) {
            "/hello" -> createHelloWorldSlice(sliceUri)
            "/test" -> createTestSlice(sliceUri)
            else -> null
        }
    }

    override fun onCreateSliceProvider() = true

    override fun onMapIntentToUri(intent: Intent?): Uri {
        super.onMapIntentToUri(intent)
        val path = intent?.data?.path ?: ""
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(context.packageName)
            .appendPath(path)
            .build()
    }

    private fun createHelloWorldSlice(sliceUri: Uri): Slice {
        return ListBuilder(context, sliceUri, ListBuilder.INFINITY)
            .setHeader {
                it.apply {
                    setTitle("Hello World")
                }
            }.build()
    }


    private fun createTestSlice(sliceUri: Uri): Slice {
        val activityAction = SliceAction(
            PendingIntent.getActivity(
                context, 0,
                MainActivity.getIntent(context), 0
            ),
            IconCompat.createWithResource(
                context,
                R.drawable.ic_arrow_forward_black_24dp
            ),
            "Go to app."
        )
        return ListBuilder(context, sliceUri, SliceHints.INFINITY)
            .setAccentColor(0x7f040047)
            .setHeader {
                it.apply {
                    setTitle("Test Slice")
                    setSubtitle("Slice for testing purposes")
                    setSummary("Welcome to the basic Slice presenter.")
                }
            }
            .addRow {
                it.apply {
                    setTitle("Example Row")
                    setSubtitle("Row Subtitle")
                    addEndItem(
                        IconCompat.createWithResource(
                            context, R.drawable.ic_arrow_forward_black_24dp
                        ), ICON_IMAGE
                    )
                }
            }
            .addAction(activityAction)
            .build()
    }
}