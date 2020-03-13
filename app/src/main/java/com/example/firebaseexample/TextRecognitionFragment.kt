package com.example.firebaseexample


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.fragment_text_recognition.*


/**
 * A simple [Fragment] subclass.
 */
class TextRecognitionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_recognition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recognize_text.setOnClickListener {
            recognizeText((text_image.drawable as BitmapDrawable).bitmap)
        }
    }

    private fun recognizeText(bitmap: Bitmap){
        recognize_text.isEnabled = false
        recognized_text.text = ""

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image)
            .addOnSuccessListener { texts ->
                parseRecognizedText(texts)
                recognize_text.isEnabled = true
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireActivity(), "Error occurred: {${e.message}}",
                    Toast.LENGTH_SHORT).show()
                recognize_text.isEnabled = true
            }
    }

    private fun parseRecognizedText(result: FirebaseVisionText){
        //recognize_text.text = result.text
        for (block in result.textBlocks) {
            //recognized_text.append("${block.text}\n")

            for (line in block.lines) {
                recognized_text.append("${line.text}\n")

                for (element in line.elements) {
                    val elementText = "${element.text}\n"
                }
            }
        }
    }
}
