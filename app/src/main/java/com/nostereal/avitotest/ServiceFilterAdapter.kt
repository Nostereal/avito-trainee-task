package com.nostereal.avitotest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_services_filter.view.*

class ServiceFilterAdapter : RecyclerView.Adapter<ServiceFilterAdapter.ServiceFilterViewHolder>() {

    private var servicesList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ServiceFilterViewHolder(parent)

    override fun getItemCount(): Int = servicesList.size


    class ServiceFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        constructor(parent: ViewGroup) :
                this(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_services_filter,
                        parent,
                        false
                    )
                )

        fun bind(service: String) {
            itemView.service_checked_tv.apply {
                text = service

                setOnClickListener {
                    this.toggle()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ServiceFilterViewHolder, position: Int) {
        holder.bind(servicesList[position])
    }

    fun addServicesCollection(services: Collection<String>) {
        servicesList.addAll(services)
    }

    fun updateServices(services: List<String>) {
        servicesList = services.toMutableList()
    }

    fun getServiceAt(position: Int) = servicesList[position]

}