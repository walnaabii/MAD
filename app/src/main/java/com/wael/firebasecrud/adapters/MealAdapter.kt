package com.wael.firebasecrud.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wael.firebasecrud.R

import com.wael.firebasecrud.models.Meal
import com.wael.firebasecrud.models.MealLog
import com.wael.firebasecrud.utils.SharedPreferencesHelper

import org.json.JSONException
import org.json.JSONObject


class MealAdapter(type:String,mealList: List<Meal>, context:Context) : RecyclerView.Adapter<MealAdapter.NewsViewHolder>() {
    private val mealList: List<Meal>
    private val context:Context
    private val type:String
    private lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    init {
        this.type = type
        this.context = context
        this.mealList = mealList
        databaseReference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.meal_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (type=="none"){
            holder.addbtn.visibility = View.GONE
        }else{
            holder.addbtn.visibility = View.VISIBLE
            holder.addbtn.text = "Add $type"
        }
        holder.setObjects(mealList[position].name,mealList[position].calories.toString())
        holder.addbtn.setOnClickListener {
            var mealLog = MealLog(sharedPreferencesHelper.getStringData("id",""),
                mealList[position].name,
                mealList[position].calories,
                mealList[position].time,
                System.currentTimeMillis())
            saveMealToDatabase(mealLog)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return mealList.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mealname: TextView
        var mealcalories:TextView
        var addbtn:TextView

        init {
            addbtn = itemView.findViewById<TextView>(R.id.addBtn)
            mealname = itemView.findViewById<TextView>(R.id.mealname)
            mealcalories = itemView.findViewById<TextView>(R.id.mealcaloreis)
        }
        @SuppressLint("ClickableViewAccessibility")
        fun setObjects(name: String,calories: String) {
            mealname.text = name
            mealcalories.text = "calories: $calories"
        }

    }

    //save record in database
    private fun saveMealToDatabase(meal: MealLog) {
        val mealsReference = databaseReference.child("mealLogs")
        val mealKey = mealsReference.push().key ?: return
        // Save the meal data to the database
        mealsReference.child(mealKey).setValue(meal)
            .addOnSuccessListener {
                // Data successfully saved
                Toast.makeText(context,"meal added successfully",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show()
            }
    }
}