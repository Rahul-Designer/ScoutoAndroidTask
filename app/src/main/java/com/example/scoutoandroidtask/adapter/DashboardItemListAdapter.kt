package com.example.scoutoandroidtask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scoutoandroidtask.R
import com.example.scoutoandroidtask.model.DashboardListview

class DashboardItemListAdapter(var context: Context,private var arrCar :ArrayList<DashboardListview>,private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DashboardItemListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var makerCar: TextView
        var modeCar: TextView
        var imageCar: ImageView
        var addCarImage: Button
        var deleteCar: Button


        init {
            makerCar = itemView.findViewById(R.id.car_maker)
            modeCar = itemView.findViewById(R.id.car_model)
            imageCar = itemView.findViewById(R.id.car_image)
            addCarImage = itemView.findViewById(R.id.car_image_btn)
            deleteCar = itemView.findViewById(R.id.delete_car)
        }

        fun bind(info: DashboardListview) {
            imageCar.setImageBitmap(info.Car_Image)
            makerCar.text = info.Make_Name
            modeCar.text = info.Model_Name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.car_item_layout, parent, false),onItemClickListener
        )
    }

    override fun getItemCount(): Int {
        return arrCar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = arrCar[position]
        holder.bind(pos)
        holder.deleteCar.setOnClickListener {
            onItemClickListener.deleteItem(position)
        }
        holder.addCarImage.setOnClickListener {
            onItemClickListener.addImage(position)
        }
    }


    //    fun updateDataList(make_name: String, model_name: String) {
//        arrCar.clear()
//        arrCar.addAll(DashboardListview(make_name, model_name))
//        notifyDataSetChanged()
//    }
interface OnItemClickListener {
    fun deleteItem(position: Int)
    fun addImage(position: Int)

}
}