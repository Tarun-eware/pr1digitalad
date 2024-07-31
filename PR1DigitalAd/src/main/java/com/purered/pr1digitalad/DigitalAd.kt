package com.purered.pr1digitalad

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

//
//data class SpotClickPayload(
//    val itemType: String,
//    val id: Int,
//    val headline: String,
//    val bodyCopy: String,
//    val imageURL: String,
//    val pricingHTML: String,
//    val pricingText: String,
//    val upc: String,
//    val startDate: String,
//    val endDate: String,
//    val category: String,
//    val disclaimer: String,
//    val webURL: String,
//    val appURL: String
//)


data class DigitalAdInput(
    val apiKey:String,
    val storeKey:String,
    val viewMode:String,
    val cartItems: List<Map<String, Any>>?,
    val clippedCoupons: List<Map<String, Any>>?,
    var payloadJsonString:String?,
    var callbackHandler: (String, JSONArray) -> Unit
)

class DigitalAd  @JvmOverloads constructor(
    context: Context,
    options:DigitalAdInput,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {

    private val webView: WebView
    private val config :DigitalAdInput = options

    var parentContext = context

    var viewRef: WebView? = null
        get() = webView

    init {
        webView = WebView(context, attrs, defStyleAttr)


        WebView.setWebContentsDebuggingEnabled(true)
        init(context)
    }
    fun convertMapToJsonString(map: DigitalAdInput): String {
        val gson = Gson()
        return gson.toJson(map)
    }
    private fun init(context: Context) {
        // Initialize WebView settings, such as enabling JavaScript
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportZoom(true)
        webView.requestFocus(View.FOCUS_DOWN);
        webView.focusable = View.FOCUSABLE_AUTO
        webView.setFocusableInTouchMode(true)
        webView.requestFocusFromTouch()




        webView.addJavascriptInterface(this,"pr1NativeWrapper")

        // Validate Client with API call then load WebView..
        val url:String ="https://oms-kroger-webapp-oms-qa.azurewebsites.net/public/DACpublic.html?id=11847"
        //val url:String ="https://pr1-std-digitalad-dev-client-app.azurewebsites.net/examples/riteaiddev.html?key=pgH7QzFHJx4w46fI~5Uzi4RvtTwlEXp3"
        //  val url:String = "https://pr1-std-digitalad-dev-client-app.azurewebsites.net/native/index.html?env=aos"
        loadUrl(url)


        webView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                var optionsJSONString:String =  convertMapToJsonString(config)

                val javascriptCode = """ 
                                    initDigitalAd($optionsJSONString)
                                """.trimIndent()
                super.onPageFinished(view, url)
                webView.evaluateJavascript(javascriptCode, null)

                //  Toast.makeText( context," DigitalAd Initiated Successfully ", Toast.LENGTH_LONG).show()

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                // super.onReceivedError(view, request, error)
                print(error)

                Toast.makeText(context,"Error : while loading webview.!!", Toast.LENGTH_LONG).show()
            }
        }
        // Other WebView configurations can be applied here
    }

    fun getViewReference(): WebView {
        return webView
    }


    fun updateLayoutParams(layoutParams: LinearLayout.LayoutParams){

        webView.layoutParams = layoutParams
    }

    fun validateCreds(name:String,password:String){
        webView.evaluateJavascript("validateCreds('${name}', '${password}');"){returnValue:String->{
            Toast.makeText(this.parentContext,returnValue, Toast.LENGTH_LONG).show()
        }}

    }

    fun dispatch(action: String,payload: String){
        val payLoadString:String =  payload
        print(action)
        print(payLoadString)
        webView.evaluateJavascript("digitalAdHandler('${action}','${payLoadString}');"){returnValue:String->{
            Toast.makeText(this.parentContext,returnValue, Toast.LENGTH_LONG).show()
        }}
    }
    fun dispatch(action: String,payload: JSONArray){
        val payLoadString:String =  payload.toString()
        print(action)
        print(payLoadString)
        webView.evaluateJavascript("digitalAdHandler('${action}','${payLoadString}');"){returnValue:String->{
            Toast.makeText(this.parentContext,returnValue, Toast.LENGTH_LONG).show()
        }}
    }
    fun dispatch(action: String,payload: JSONObject){
        val payLoadString:String =  payload.toString()
        print(action)
        print(payLoadString)
        webView.evaluateJavascript("digitalAdHandler('${action}','${payLoadString}');"){returnValue:String->{
            Toast.makeText(this.parentContext,returnValue, Toast.LENGTH_LONG).show()
        }}
    }



    @JavascriptInterface
    fun callBackHandler(actionType:String,payloadJsonString: String):String{
        println(actionType)
        println(payloadJsonString)


        var isSuccess:Boolean = false;
        var error:String = "";

        try {
            val jsonObject: JSONArray = JSONArray("[]")
            config.callbackHandler(actionType,jsonObject);
            isSuccess = true
        } catch (e: Exception) {
            println("Exception occurred: ${e.message}")
            isSuccess = false;
            error = e.message ?: ""
        }

        return "{'isSuccess': $isSuccess , 'error' : $error}"
    }


    @JavascriptInterface
    fun showToastMsg(msg:String){
        Toast.makeText(this.parentContext,msg, Toast.LENGTH_LONG).show()
    }

    // Method to load a URL into the WebView
    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    // Other methods to handle WebView events or customize behavior can be added here
}