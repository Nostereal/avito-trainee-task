package com.nostereal.avitotest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostereal.avitotest.models.ParcelableSet
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.item_services_filter.view.*

class FilterActivity : AppCompatActivity() {

    companion object {
        const val FILTERED_SERVICES_SET_RESULT_EXTRA = "filteredServices"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val serviceAdapter = ServiceFilterAdapter()

        intent?.also {
            val allServicesSet: Set<String> =
                it.getParcelableExtra<ParcelableSet>(MapsActivity.SERVICE_SET_EXTRA_NAME)?.data
                    ?: emptySet()
            val servicesToShowSet: Set<String> =
                it.getParcelableExtra<ParcelableSet>(MapsActivity.SERVICES_TO_SHOW_SET_EXTRA_NAME)?.data
                    ?: emptySet()

            serviceAdapter.apply {
                addServicesCollection(allServicesSet)
                // to restore filter state
                selectedServicesSet = servicesToShowSet
            }
        }

        rv_services.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = serviceAdapter
        }

        // handle saving filters and passing it to the map activity
        btn_saveFilter.setOnClickListener {
            val selectedServices: MutableSet<String> = mutableSetOf()

            Log.d("FilterActivity", "Adapter items count = ${serviceAdapter.itemCount}")
            for (pos in 0 until serviceAdapter.itemCount) {
                val viewHolder = rv_services.findViewHolderForAdapterPosition(pos)!!
                val checkedTextView = viewHolder.itemView.service_checked_tv

                if (checkedTextView.isChecked) {
                    selectedServices.add(serviceAdapter.getServiceAt(pos))
                }
            }

            val resultIntent = Intent().apply {
                putExtra(FILTERED_SERVICES_SET_RESULT_EXTRA, ParcelableSet(selectedServices))
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
