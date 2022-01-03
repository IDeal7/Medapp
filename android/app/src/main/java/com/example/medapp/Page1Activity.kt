package com.example.medapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.medapp.databinding.ActivityNavDrawerBinding
import com.example.medapp.databinding.ActivityPage1Binding
import com.example.medapp.databinding.ListviewBinding
import java.util.ArrayList

class Page1Activity : AppCompatActivity() {
    var listView: ListView? = null
    override fun onResume() {
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page1)
        listView = findViewById<View>(R.id.listView) as ListView
        val items = ArrayList<String>()
        items.add("애드빌(advil)")
        items.add("베아제(beaze)")
        items.add("센트롬(centrum)")
        items.add("크레로바정(crerova)")
        items.add("세파메칠정(cephamethyl)")
        items.add("임팩타민(impactamin)")
        items.add("캐롤에프(kaloef)")
        items.add("설페린F(selferinF)")
        items.add("써스펜(suspen)")
        items.add("메카인(mekain)")

        val adapter = CustomAdapter(this, 0, items)
        listView!!.adapter = adapter


    }

    private inner class CustomAdapter(
        context: Context?,
        textViewResourceId: Int,
        private val items: ArrayList<String>
    ) : ArrayAdapter<String?>(
        context!!, textViewResourceId, items as List<String?>
    ) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v = convertView
            if (v == null) {
                val vi = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                v = vi.inflate(R.layout.listview, null)
            }
            // ImageView 인스턴스
            val imageView = v!!.findViewById<View>(R.id.imageView) as ImageView
            // 리스트뷰의 아이템에 이미지를 변경한다.
            if ("애드빌(advil)" == items[position]) imageView.setImageResource(R.drawable.advil)
            else if ("센트롬(centrum)" == items[position]) imageView.setImageResource(R.drawable.centrum)
            else if ("세파메칠정(cephamethyl)" == items[position]) imageView.setImageResource(R.drawable.cephamethyl)
            else if ("임팩타민(impactamin)" == items[position]) imageView.setImageResource(R.drawable.impactamin)
            else if ("써스펜(suspen)" == items[position]) imageView.setImageResource(R.drawable.suspen)
            else if ("베아제(beaze)" == items[position]) imageView.setImageResource(R.drawable.beaze)
            else if ("크레로바정(crerova)" == items[position]) imageView.setImageResource(R.drawable.crerova)
            else if ("캐롤에프(kaloef)" == items[position]) imageView.setImageResource(R.drawable.kaloef)
            else if ("메카인(mekain)" == items[position]) imageView.setImageResource(R.drawable.mekain)
            else if ("설페린F(selferinF)" == items[position]) imageView.setImageResource(R.drawable.selfreinf)

            val textView = v.findViewById<View>(R.id.textView) as TextView
            textView.text = items[position]
            val text = items[position]
            /*Button button = (Button)v.findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            });*/return v
        }
    }
}