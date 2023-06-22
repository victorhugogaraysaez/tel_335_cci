package com.example.comoconectarseainternet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.example.comoconectarseainternet.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var urlList: List<String>
    private lateinit var progessBarList: List<ProgressBar>
    private lateinit var imageViewList: List<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoad()
        initProgessBar()
        initImageViews()

        binding.btnMessage.setOnClickListener {
            AlertDialog
                .Builder(this)
                .setMessage("Si puede ver este mensaje, se demuestra el funcionamiento de las corrutinas en background")
                .setCancelable(true)
                .show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            for (i in urlList.indices) {
                val image = doInBackground(urlList[i], progessBarList[i])
                Log.d("Corrutinas", image.toString())

                if (image != null) {
                    updateView(image, progessBarList[i], imageViewList[i])
                }
            }
        }

    }

    private fun imageLoad() {
        urlList = listOf<String>(
            "https://www.telegraph.co.uk/content/dam/news/2023/02/09/TELEMMGLPICT000324730122_trans_NvBQzQNjv4BqR19G9Wkz1kuyhSe1xOMz-J-adDIVBzhyFO8H9Zf04-A.jpeg?imwidth=960",
            "https://www3.gobiernodecanarias.org/medusa/ecoblog/msuaump/files/2012/11/desierto-atacama.jpg",
            "https://galeriabucci.cl/wp-content/themes/yootheme/cache/shutterstock_365514371-ba19307b.jpeg",
            "https://www.telegraph.co.uk/content/dam/news/2023/02/09/TELEMMGLPICT000324730122_trans_NvBQzQNjv4BqR19G9Wkz1kuyhSe1xOMz-J-adDIVBzhyFO8H9Zf04-A.jpeg"
        )
    }

    private fun initProgessBar() {
        progessBarList = listOf(binding.pBarImg1, binding.pBarImg2, binding.pBarImg3, binding.pBarImg4)
    }

    private fun initImageViews() {
        imageViewList = listOf(binding.img1, binding.img2, binding.img3, binding.img4)
    }

    private suspend fun doInBackground(url: String, progressBar: ProgressBar): Bitmap {
        lateinit var bmp: Bitmap
        withContext(Dispatchers.Default) {
            try {
                progressBar.visibility = View.VISIBLE
                val newURL = URL(url)
                val inputStram = newURL.openConnection().getInputStream()

                Log.d("Corrutinas", inputStram.toString())

                bmp = BitmapFactory.decodeStream(inputStram)
            } catch (e: Exception) {
                Log.d("Corrutinas", e.message.toString())
                e.printStackTrace()
            }
        }
        return bmp
    }

    private fun updateView(image: Bitmap, progressBar: ProgressBar, imageView: ImageView) {
        Log.d("Corrutinas", "Ahora se puede ver lo del updateView ")
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(image)
    }
}