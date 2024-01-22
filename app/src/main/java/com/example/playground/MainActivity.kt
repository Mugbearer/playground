package com.example.playground

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.playground.ml.Model
import com.example.playground.ui.theme.PlaygroundTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun App() {
    Playground(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

private fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap {
    val inputStream: InputStream = contentResolver.openInputStream(uri)!!
    return BitmapFactory.decodeStream(inputStream)
}

@Composable
fun Playground(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    // ActivityResultLauncher for image selection
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            if (uri != null) {
                // Load the selected image into a Bitmap
                selectedBitmap = loadBitmapFromUri(context.contentResolver, uri)
            }
        }

    var bitmap: Bitmap

    var imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(28, 28, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    Column {
        selectedImageUri?.let { uri ->
            val painter: Painter = rememberImagePainter(uri)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp, 300.dp)
                    .fillMaxSize()
            )
        }

        //Image Selected
        Button(onClick = {
            getContent.launch("image/*")
        }) {
            Text(text = "Select Image")
        }
        Button(onClick = {
//            var tensorImage = TensorImage(DataType.FLOAT32)
//            tensorImage.load(selectedBitmap)
//
//            val model = Model.newInstance(context)
//
//            val inputFeature0 =
//                TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
//            inputFeature0.loadBuffer(tensorImage.buffer)
//
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//            var maxIdx = 0
//            outputFeature0.forEachIndexed { index, fl ->
//                if(outputFeature0[maxIdx] < fl)
//            }
//
//            model.close()
        }) {
            Text(text = "Predict")
        }
        Text(text = "Prediction: $result")
    }
}