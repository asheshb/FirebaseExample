package com.example.firebaseexample


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import kotlinx.android.synthetic.main.fragment_label_image.*


/**
 * A simple [Fragment] subclass.
 */
class LabelImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recognize_labels.setOnClickListener {
            recognizeLabels((label_image.drawable as BitmapDrawable).bitmap)
        }
    }

    private fun recognizeLabels(bitmap: Bitmap){
        recognize_labels.isEnabled = false
        recognized_labels.text = ""

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

        labeler.processImage(image)
            .addOnSuccessListener { labels ->
                parseRecognizedLabels(labels)
                recognize_labels.isEnabled = true
            }
            .addOnFailureListener { e ->

                Toast.makeText(requireActivity(), "Error occurred: {${e.message}}",
                    Toast.LENGTH_SHORT).show()
                recognize_labels.isEnabled = true
            }
    }

    private fun parseRecognizedLabels(labels: List<FirebaseVisionImageLabel>){
        for (label in labels) {
            val text = label.text
            val confidence = label.confidence

            //Knowledge Graph entity ID (if available)
            val entityId = label.entityId

            recognized_labels.append(getString(R.string.image_labels_text, text, confidence))
        }
    }

}
