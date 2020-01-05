package com.nostereal.avitotest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostereal.avitotest.models.ParcelableSet
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.item_services_filter.view.*

class FilterActivity : AppCompatActivity() {

    private lateinit var serviceAdapter: ServiceFilterAdapter
    private lateinit var serviceLayoutManager: LinearLayoutManager

    private lateinit var recyclerViewState: Parcelable

    companion object {
        const val FILTERED_SERVICES_SET_RESULT_EXTRA = "filteredServices"

        private const val SELECTED_SERVICES_KEY = "selectedServices"
        private const val RECYCLER_VIEW_STATE_KEY = "rvState"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        serviceAdapter = ServiceFilterAdapter()
        serviceLayoutManager = LinearLayoutManager(this)

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
            val selectedServices: Set<String> = getSelectedTextViews(serviceAdapter)

            val resultIntent = Intent().apply {
                putExtra(FILTERED_SERVICES_SET_RESULT_EXTRA, ParcelableSet(selectedServices))
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("FilterActivity", "Restoring state...")
        serviceAdapter.selectedServicesSet =
            savedInstanceState.getParcelable<ParcelableSet>(SELECTED_SERVICES_KEY)!!.data
        recyclerViewState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY)!!
    }

    override fun onResume() {
        super.onResume()
        if (::recyclerViewState.isInitialized)
            rv_services.layoutManager!!.onRestoreInstanceState(recyclerViewState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("FilterActivity", "Saving state...")
        outState.putParcelable(
            SELECTED_SERVICES_KEY,
            ParcelableSet(getSelectedTextViews(serviceAdapter))
        )
        Log.d("FilterActivity", "Selected TVs: ${getSelectedTextViews(serviceAdapter)}")
        outState.putParcelable(
            RECYCLER_VIEW_STATE_KEY,
            rv_services.layoutManager!!.onSaveInstanceState()
        )
        super.onSaveInstanceState(outState)
    }

    private fun getSelectedTextViews(adapter: ServiceFilterAdapter): Set<String> {
        val selectedServices = mutableSetOf<String>()

        Log.d("FilterActivity", "Adapter items count = ${adapter.itemCount}")
        rv_services.forEachIndexed { pos, view ->
            if (view.service_checked_tv.isChecked)
                selectedServices.add(adapter.getServiceAt(pos))
        }
        return selectedServices
    }
}
