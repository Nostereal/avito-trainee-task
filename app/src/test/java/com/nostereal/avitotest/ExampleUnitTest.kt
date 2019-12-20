package com.nostereal.avitotest

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nostereal.avitotest.models.PinsData
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    val context: Context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun isGsonParsingCorrect() {
        val json = context.assets.open("pins.json").readBytes().joinToString()
        val pinsData = Gson().fromJson(json, PinsData::class.java)

        assertThat(pinsData.pins[0].id).isEqualTo(1)
        assertThat(pinsData.pins[0].coordinates.latitude).isEqualTo(55.725432)
    }
}
