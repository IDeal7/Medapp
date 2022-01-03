package com.example.medapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.medapp.databinding.ActivitySecondBinding
import com.example.medapp.databinding.ActivityThirdBinding
import org.tensorflow.lite.Interpreter


import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class ThirdActivity : AppCompatActivity() {

    // ViewBinding
    lateinit var binding : ActivityThirdBinding
    // Permisisons
    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100
    // Request Code
    private val BUTTON1 = 100

    // 원본 사진이 저장되는 Uri
    private var photoUri: Uri? = null

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThirdBinding.inflate(layoutInflater)
        val view = binding.root

        binding.button6.setOnClickListener {
            val intent = Intent(this, EffectActivity::class.java)

            intent.putExtra("key",index.toString())
            startActivity(intent)

        }

        setContentView(view)
        checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)

        binding.gallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*" // 이미지만
            intent.action = Intent.ACTION_GET_CONTENT // 카메라(ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, FROM_ALBUM)
        }
        binding.camerabtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, BUTTON1)
            }


        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                BUTTON1 -> {
                    //미리보기용 사진,bitmap은 압축안한사진 보여줌
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.imageView.setImageBitmap(imageBitmap)

                    val bmp = Bitmap.createScaledBitmap(imageBitmap, 128, 128, true)




                    val batchNum = 0
                    val input = Array(1) { Array(128) { Array(128) { FloatArray(3) } } }
                    for (x in 0..127) {
                        for (y in 0..127) {
                            val pixel = bmp.getPixel(x, y)
                            // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                            // model. For example, some models might require values to be normalized
                            // to the range [0.0, 1.0] instead.
                            input[batchNum][x][y][0] = Color.red(pixel) / 1.0f
                            input[batchNum][x][y][1] = Color.green(pixel) / 1.0f
                            input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f
                        }
                    }
                    // 파이썬에서 만든 모델 파일 로딩
                    val tf_lite = getTfliteInterpreter("model_9.tflite")

                    // 케라스로부터 변환할 때는 입력이 명시되지 않기 때문에 입력을 명확하게 정의할 필요가 있다.
                    // 이때 getInputTensor 함수를 사용한다. getOutputTensor 함수도 있다.
                    // 입력은 1개밖에 제공하지 않았고, 784의 크기를 갖는 1차원 이미지.
                    // 입력이 1개라는 뜻은 getInputTensor 함수에 전달할 인덱스가 0밖에 없다는 뜻이다.
                    // 여러 장의 이미지를 사용하면 shape에 표시된 1 대신 이미지 개수가 들어간다.
                    // input : [1, 16384];
                    //Tensor input = tf_lite.getInputTensor(0);
                    //Log.d("input", Arrays.toString(input.shape()));

                    // 출력 배열 생성. 1개만 예측하기 때문에 [1] 사용
                    // bytes_img에서처럼 1차원으로 해도 될 것 같은데, 여기서는 에러.
                    val output = Array(1) { FloatArray(6) }
                    //Tensor out_put = tf_lite.getOutputTensor(0);
                    //Log.d("output ", Arrays.toString(out_put.shape()));
                    tf_lite!!.run(input, output)

                    //try (Interpreter interpreter = new Interpreter(new File("model_4.tflite"))) {
                    //    interpreter.run(iv, output);

                    // } catch (Exception e) {
                    //   e.printStackTrace();
                    //}
                    Log.d("predict", Arrays.toString(output[0]))

                    // 텍스트뷰 10개. 0~9 사이의 숫자 예측
                    //int[] id_array = { R.id.result_1, R.id.result_2, R.id.result_3, R.id.result_4,
                    //        R.id.result_5, R.id.result_6}; //, R.id.result_0,, R.id.result_7, R.id.result_8, R.id.result_9};
                    val id_array = intArrayOf(R.id.result1)
                    val Medicine_array = arrayOf("advil", "dp", "suspen","imfectamin", "centrum")
                    var index = 0
                    var max = .0
                    for (i in 0..4) {
                        //float Max = 0;
                        val tv = findViewById<TextView>(id_array[0])
                        //if (output[0][i] >= 0.8) {
                        //Max = output[0][i];
                        //    Log.d("output", Arrays.toString(floatArrayOf(output[0][i])))
                        //    index = i
                        //}
                        if (max < output[0][i]) {
                            index = i
                            max = output[0][i].toDouble()
                        }
                        Log.d("index", index.toString())
                        tv.text = Medicine_array[index]
                        val a=index

                    }


                }
                FROM_ALBUM->{
                    // 선택한 이미지에서 비트맵 생성
                    val stream = contentResolver.openInputStream(data!!.data!!)
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 4
                    val src = BitmapFactory.decodeStream(stream)
                    val bmp = Bitmap.createScaledBitmap(src, 128, 128, true)

                    //Bitmap bmp = BitmapFactory.decodeStream(stream);
                    stream!!.close()
                    val iv = findViewById<ImageView>(R.id.imageView)
                    iv.scaleType = ImageView.ScaleType.FIT_XY // [300, 300]에 꽉 차게 표시
                    iv.setImageBitmap(bmp)

                    // ---------------------------------------- //
                    // 검증 코드. 여러 차례 변환하기 때문에 PC 버전과 같은지 확인하기 위해 사용.

                    // mnist 원본은 0~1 사이의 실수를 사용해 픽셀을 표현한다. 픽셀 1개에 1바이트가 아니라 4바이트 사용.
                    // 메모리 용량은 3136(28 * 28 * 4). 입력 이미지를 똑같이 만들어서 전달해야 한다.

                    // mnist에서 생성한 숫자 이미지 파일이 흑백이긴 하지만 ARGB를 사용해서 색상을 표시하기 때문에
                    // 가운데 픽셀의 경우 fffcfcfc와 같은 형태로 나온다.
                    // ff는 alpha를 가리키고 동일한 값인 fc가 RGB에 공통으로 나온다.

                    // getPixel 함수는 int를 반환하기 때문에 부호 없는 16진수로 확인해야 한다.
                    // int pixel = bmp.getPixel(14, 14);
                    // Log.d("getPixel", Integer.toUnsignedString(pixel, 16));

                    // 원본 mnist 이미지는 (28, 28, 1)로 되어 있다.
                    // getByteCount 함수로 확인해 보면 3136으로 나오는데
                    // 각각의 픽셀이 4바이트로 구성되어 있기 때문에 그렇다. 784 * 4 = 3136
                    // int bytes = bmp.getByteCount();
                    // Log.d("getByteCount", Integer.toString(bytes));

                    // mnist 원본 이미지와 비교하기 위해 줄 단위로 변환 결과 출력
                    // 파이썬에서 똑같은 파일을 읽어들여서 에뮬레이터 출력과 비교. 똑같이 나온다. 성공.
                    // 2차원 배열을 한 번에 깔끔하게 출력할 수 없기 때문에 아래 코드가 필요하다.
                    // float[] row = new float[28];
                    // for(int y = 0; y < 28; y++) {
                    //     for(int x = 0; x < 28; x++) {
                    //         int pixel = bmp.getPixel(x, y);         // x가 앞쪽, y가 뒤쪽.
                    //         row[x] = (pixel & 0xff) / (float) 255;  // 실수 변환하지 않으면 0과 1로만 나온다.
                    //     }
                    //     // 줄 단위 출력. 그래도 자릿수가 맞지 않아 numpy처럼 나오진 않는다.
                    //     Log.d(String.format("%02d", y), Arrays.toString(row));
                    // }

                    // ---------------------------------------- //

                    //int bytes = bmp.getByteCount();
                    //Log.d("getByteCount", Integer.toString(bytes));
                    // 비트맵 이미지로부터 RGB에 해당하는 값을 1개만 가져와서
                    // mnist 원본과 동일하게 0~1 사이의 실수로 변환하고, 1차원 784로 만들어야 한다.
                    // 그러나, 실제로 예측할 때는 여러 장을 한 번에 전달할 수 있어야 하기 때문에
                    // 아래와 같이 2차원 배열로 만드는 것이 맞다.
                    // 만약 1장에 대해서만 예측을 하고 싶다면 1차원 배열로 만들어도 동작한다.
                    //float[][] bytes_img = new float[1][16384];
                    //val bytes_img = Array(1) { Array(128) { Array(128) { FloatArray(3) } } }

                    //for (y in 0..127) {
                    //    for (x in 0..127) {
                    //        val pixel = bmp.getPixel(x, y)
                    //        // bytes_img[0][y * 128 + x] = (pixel & 0xff) / (float) 255;
                    //        bytes_img[0][x][y][0] = (Color.red(pixel) - 254) / 255.toFloat()
                    //        bytes_img[0][x][y][1] = (Color.green(pixel) - 254) / 255.toFloat()
                    //        bytes_img[0][x][y][2] = (Color.blue(pixel) - 254) / 255.toFloat()
                    //    }
                    //}

                    val bitmap = Bitmap.createScaledBitmap(src, 128, 128, true)

                    val batchNum = 0
                    val input = Array(1) { Array(128) { Array(128) { FloatArray(3) } } }
                    for (x in 0..127) {
                        for (y in 0..127) {
                            val pixel = bitmap.getPixel(x, y)
                            // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                            // model. For example, some models might require values to be normalized
                            // to the range [0.0, 1.0] instead.
                            input[batchNum][x][y][0] = Color.red(pixel) / 1.0f
                            input[batchNum][x][y][1] = Color.green(pixel) / 1.0f
                            input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f
                        }
                    }
                    // 파이썬에서 만든 모델 파일 로딩
                    val tf_lite = getTfliteInterpreter("model_9.tflite")



                    // 출력 배열 생성. 1개만 예측하기 때문에 [1] 사용
                    // bytes_img에서처럼 1차원으로 해도 될 것 같은데, 여기서는 에러.
                    val output = Array(1) { FloatArray(6) }
                    //Tensor out_put = tf_lite.getOutputTensor(0);
                    //Log.d("output ", Arrays.toString(out_put.shape()));
                    tf_lite!!.run(input, output)

                    //try (Interpreter interpreter = new Interpreter(new File("model_4.tflite"))) {
                    //    interpreter.run(iv, output);

                    // } catch (Exception e) {
                    //   e.printStackTrace();
                    //}
                    Log.d("predict", Arrays.toString(output[0]))

                    // 텍스트뷰 10개. 0~9 사이의 숫자 예측
                    //int[] id_array = { R.id.result_1, R.id.result_2, R.id.result_3, R.id.result_4,
                    //        R.id.result_5, R.id.result_6}; //, R.id.result_0,, R.id.result_7, R.id.result_8, R.id.result_9};
                    val id_array = intArrayOf(R.id.result1)
                    val Medicine_array = arrayOf("advil", "dp", "suspen", "imfectamin", "centrum")

                    var max = .0
                    for (i in 0..4) {
                        //float Max = 0;
                        val tv = findViewById<TextView>(id_array[0])
                        //if (output[0][i] >= 0.8) {
                        //Max = output[0][i];
                        //    Log.d("output", Arrays.toString(floatArrayOf(output[0][i])))
                        //    index = i
                        //}
                        if (max < output[0][i]) {
                            index = i
                            max = output[0][i].toDouble()
                        }
                        Log.d("index", index.toString())
                        tv.text = Medicine_array[index]


                    }

                }


            }
        }
    }
    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList : MutableList<String> = mutableListOf()
        for(permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission)
            }
        }
        if(permissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSIONS_REQUEST)
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
            if(result != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private fun getTfliteInterpreter(modelPath: String): Interpreter? {
        try {
            return Interpreter(loadModelFile(this@ThirdActivity, modelPath))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity, modelPath: String): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    companion object {
        private const val FROM_ALBUM = 1 // onActivityResult 식별자
        private const val FROM_CAMERA = 2 // 카메라는 사용 안함
    }

}
