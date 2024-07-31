package com.purered.pr1digitaladlib

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.purered.pr1digitalad.DigitalAd
import com.purered.pr1digitalad.DigitalAdInput
import com.purered.pr1digitaladlib.ui.theme.PR1DigitalAdLibTheme
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        val cartItems = listOf<Map<String, Any>>(
            mapOf( "productId" to "0350512", "quantity" to 1 ),
            mapOf( "productId" to "0385494", "quantity" to 2 )
        );
        val clippedCoupons = listOf<Map<String, Any>>(
            mapOf( "couponId" to "70f6af3f-1b6d-452e-b699-d62020b8c7e4",  "id" to "8ea69008-aa35-4d2b-a075-9012b45e835f"  ),

            //  mapOf( "couponId" to "70af9ac4-50e8-499b-b9e4-7f2dd4b10351",  "id" to "e61f961d-abc9-4a33-a88d-23ac36d83756"  )
        );
        val options: DigitalAdInput =   DigitalAdInput(  "pgH7QzFHJx4w46fI~5Uzi4RvtTwlEXp3",
            "10074",
            "",
            cartItems,
            clippedCoupons,
            ""
        ){
                actionType:String,payload: JSONArray ->
            when (actionType) {
                "onClipCoupon" -> {
                    // Perform action for onClipCoupon
                    println("Performing action for onClipCoupon with payload: $payload")
                }
                "onSelectStore" -> {
                    // Perform action for onUnclipCoupon
                    println("Performing action for onUnclipCoupon")
                }
                else -> {
                    // Default case or handle other cases
                    println("Unknown case: $actionType with object: $payload")
                }
            }
        }







        // Initialise DigitalAd with the required options parameter of type DigitalAdInput
        val digitalAd = DigitalAd(this, options)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT )
        digitalAd.updateLayoutParams(layoutParams)

        //Obtain a reference to the Digital Ad view using the `viewRef` property, which can be used to render the ad on the screen.
        val adViewRef = digitalAd.viewRef;
        findViewById<LinearLayout>(R.id.container).addView(adViewRef)

        val btnValidate: View = findViewById(R.id.btnvalidate)


        btnValidate.setOnClickListener { view: View ->
//            val payload = JSONArray()
//            var couponOne = JSONObject()
//            couponOne.put("couponId" , "70af9ac4-50e8-499b-b9e4-7f2dd4b10351")
//            couponOne.put("id" , "e61f961d-abc9-4a33-a88d-23ac36d83756")
//
//            payload.put(0, couponOne)
//            // Dispatch Digital Ad Methods needs action name, action payload
//            digitalAd.dispatch("clipCoupon", payload)
            //     digitalAd.dispatch("setStore", "01301")

            digitalAd.dispatch("setClippedCoupons", "01301")


        }





    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PR1DigitalAdLibTheme {
        Greeting("Android")
    }
}