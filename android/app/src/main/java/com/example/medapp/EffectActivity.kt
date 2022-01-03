package com.example.medapp

import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*
import com.example.medapp.databinding.ActivityEffectBinding
import com.example.medapp.databinding.ActivityNavDrawerBinding
import kotlinx.coroutines.*
import java.io.InputStream

@Entity
data class NationalWeatherTable(
    @PrimaryKey val code:Int,
    val name1:String,
    val name2: String
)

@Dao
interface NationalWeatherInterface{
    @Query("SELECT * FROM NationalWeatherTable")
    fun getAll(): List<NationalWeatherTable>

    @Insert
    fun insert(nationalWeatherTable: NationalWeatherTable)

    @Query("DELETE FROM NationalWeatherTable")
    fun deleteAll()
}



@Database(entities = [NationalWeatherTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nationalWeatherInterface(): NationalWeatherInterface
}
/*
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-contacts"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}*/

class EffectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEffectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val NationalWeatherDB=Room.databaseBuilder(this,AppDatabase::class.java,"db").build()
        //파일로드


        val assetManager: AssetManager=resources.assets
        val inputStream: InputStream =assetManager.open("effect7.txt")

        inputStream.bufferedReader().readLines().forEach{ //각 라인 읽기기
            //Log.d("file_test",it.toString())

            var token=it.split("\t")
            //var input =NationalWeatherTable(token[0].toInt(),token[1],token[2])
            CoroutineScope(Dispatchers.Main).launch {
                // EffectDB.EffectInterface().insert(input)
                var data:String?
                data=intent.getStringExtra("key")
                Log.d("key",""+data)

                var a=token[data!!.toInt()]
                binding.textView4.text=a
                Log.d("file_test",a)

            }
        }
        /*CoroutineScope(Dispatchers.Main).launch {
            var output = NationalWeatherDB.nationalWeatherInterface().getAll()
            Log.d("db_test", "$output")
        }*/
        /*val input=NationalWeatherTable(111,"dfdf","fdfdf")
        NationalWeatherDB.nationalWeatherInterface().deleteAll()
        NationalWeatherDB.nationalWeatherInterface().insert(input)
        val output=NationalWeatherDB.nationalWeatherInterface().getAll()[0]
        Log.d("db_test","$output")*/
        /*val EffectDB=Room.databaseBuilder(this,AppDatabase::class.java,"db").build()

        //파일로드
        val assetManager: AssetManager=resources.assets
        val inputStream: InputStream =assetManager.open("effect5.txt")

        inputStream.bufferedReader().readLines().forEach{ //각 라인 읽기기
           //Log.d("file_test",it.toString())
            var token=it.split("\t")
            var input =EffectTable(token[0].toInt(),token[1],token[2])

           CoroutineScope(Dispatchers.Main).launch {
               // EffectDB.EffectInterface().insert(input)
               Log.d("file_test",it.toString())
            }
        }*/

        /*CoroutineScope(Dispatchers.Main).launch{
            var output=EffectDB.EffectInterface().getAll()
            Log.d("db_test","$output")
        }*/




    }



}