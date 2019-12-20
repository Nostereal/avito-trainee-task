package com.nostereal.avitotest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
            val services: Set<String> =
                it.getParcelableExtra<ParcelableSet>(MapsActivity.SERVICE_SET_EXTRA_NAME)?.data
                    ?: emptySet()
            serviceAdapter.addServicesCollection(services)
        }

        rv_services.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = serviceAdapter
        }

        btn_saveFilter.setOnClickListener {
            // handle saving filters and passing it to the map activity
            val selectedServices: MutableSet<String> = mutableSetOf()

            Log.d("FilterActivity", "Adapter items count = ${serviceAdapter.itemCount}")
            for (pos in 0 until serviceAdapter.itemCount) {
                // WARNING
                val viewHolder = rv_services.findViewHolderForAdapterPosition(pos)!!
                val checkedTextView = viewHolder.itemView.service_checked_tv

                if (checkedTextView.isChecked) {
                    selectedServices.add(serviceAdapter.getServiceAt(pos))
                }
            }

            val resultIntent = Intent().apply {
                putExtra(FILTERED_SERVICES_SET_RESULT_EXTRA, ParcelableSet(selectedServices))
            }
            Toast.makeText(this, "Selected Services: $selectedServices", Toast.LENGTH_LONG).show()

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
